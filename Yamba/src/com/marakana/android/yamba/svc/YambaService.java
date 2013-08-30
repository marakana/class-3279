package com.marakana.android.yamba.svc;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.BuildConfig;
import com.marakana.android.yamba.R;
import com.marakana.android.yamba.YambaApplication;
import com.marakana.android.yamba.YambaContract;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class YambaService extends IntentService {
    private static final String TAG = "SVC";

    private static final String PARAM_STATUS = "YambaService.STATUS";
    private static final String PARAM_OP = "YambaService.OP";
    private static final int OP_POST = -1;
    private static final int OP_POST_COMPLETE = -2;
    private static final int OP_POLL = -3;


    private static class Hdlr extends Handler {
        private final  YambaService svc;

        public Hdlr(YambaService svc) { this.svc = svc; }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OP_POST_COMPLETE:
                    svc.postComplete(msg.arg1);
                    break;
            }
        }
    }

    public static void post(Context ctxt, String status) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POST);
        i.putExtra(PARAM_STATUS, status);
        ctxt.startService(i);
    }

    public static void startPolling(Context ctxt) {
        if (BuildConfig.DEBUG) { Log.d(TAG, "start polling"); }
        ((AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE))
            .setInexactRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + 100,
                3 * 60 * 1000,
                createPendingIntent(ctxt));
    }

    public static void stopPolling(Context ctxt) {
        ((AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE))
            .cancel(createPendingIntent(ctxt));
    }

    private static PendingIntent createPendingIntent(Context ctxt) {
        Intent i = new Intent(ctxt, YambaService.class);
        i.putExtra(PARAM_OP, OP_POLL);
        return PendingIntent.getService(
                ctxt,
                OP_POLL,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private volatile Hdlr hdlr;

    public YambaService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");
        hdlr = new Hdlr(this);
    }

    void postComplete(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onHandleIntent(Intent i) {
        int op = i.getIntExtra(PARAM_OP, 0);
        if (BuildConfig.DEBUG) { Log.d(TAG, "Handle op: " + op); }
        switch (op) {
            case OP_POST:
                doPost(i.getStringExtra(PARAM_STATUS));
                break;
            case OP_POLL:
                doPoll();
                break;
            default:
                throw new IllegalStateException("Unexpected op: " + op);
        }
    }

    private void doPost(String status) {
        if (BuildConfig.DEBUG) { Log.d(TAG, "Posting: " + status); }

        int ret = R.string.post_failed;
        try {
            ((YambaApplication) getApplication()).getYambaClient().postStatus(status);
            ret = R.string.post_succeeded;
        }
        catch (YambaClientException e) {
            Log.e(TAG, "Post failed", e);
        }

        Message.obtain(hdlr, OP_POST_COMPLETE, ret, 0).sendToTarget();
    }

    private void doPoll() {
        List<Status> timeline = null;
        try { timeline = ((YambaApplication) getApplication()).getYambaClient().getTimeline(20); }
        catch (YambaClientException e) {
            Log.e(TAG, "Poll failed: ", e);
        }

        int added = processTimeline(timeline);
        if (0 < added) {
            if (BuildConfig.DEBUG) { Log.d(TAG, "inserted: " + added); }
            Intent i = new Intent(YambaContract.TIMELINE_UPDATE_BROADCAST);
            i.putExtra(YambaContract.TIMELINE_UPDATE_COUNT, added);
            sendBroadcast(i, YambaContract.TIMELINE_UPDATE_PERMISSION);
        }
    }

    private int processTimeline(List<Status> timeline) {
        if (null == timeline) { return 0; }

        long latest = getMaxTimestamp();

        List<ContentValues> rows = new ArrayList<ContentValues>();
        for (Status status: timeline) {
            long t = status.getCreatedAt().getTime();
            if (t <= latest) { continue; }

            ContentValues row = new ContentValues();
            row.put(YambaContract.Timeline.Columns.ID,
                    Long.valueOf(status.getId()));
            row.put(YambaContract.Timeline.Columns.CREATED_AT, Long.valueOf(t));
            row.put(YambaContract.Timeline.Columns.USER, status.getUser());
            row.put(YambaContract.Timeline.Columns.STATUS, status.getMessage());
            rows.add(row);
        }

        return getContentResolver().bulkInsert(
                YambaContract.Timeline.URI,
                rows.toArray(new ContentValues[rows.size()]));
    }


    // select virt1, virt2 from mytable where virt1="hi" order by virt2 asc;
    // select phys1 as virt1, phys2 as virt2 from mytable where virt1="hi" order by virt2 asc;
    private long getMaxTimestamp() {
        Cursor c = null;
        long mx = Long.MIN_VALUE;
        try {
            c = getContentResolver().query(
                    YambaContract.Timeline.URI,
                    new String[] { YambaContract.Timeline.Columns.MAX_TIMESTAMP },
                    null, null, null);
            if (c.moveToNext()) { mx = c.getLong(0); }
        }
        finally {
            if (null != c) { c.close(); }
        }

        return mx;
    }
}
