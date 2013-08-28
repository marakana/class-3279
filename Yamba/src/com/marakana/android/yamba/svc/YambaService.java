package com.marakana.android.yamba.svc;

import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.BuildConfig;
import com.marakana.android.yamba.R;
import com.marakana.android.yamba.clientlib.YambaClient;
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
                30 * 1000,
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


    private volatile YambaClient yamba;
    private volatile Hdlr hdlr;

    public YambaService() { super(TAG); }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");

        startPolling(this);

        hdlr = new Hdlr(this);

        yamba = new YambaClient("student", "password", "http://yamba.marakana.com/api");
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
            yamba.postStatus(status);
            ret = R.string.post_succeeded;
        }
        catch (YambaClientException e) {
            Log.e(TAG, "Post failed", e);
        }

        Message.obtain(hdlr, OP_POST_COMPLETE, ret, 0).sendToTarget();
    }

    private void doPoll() {
        List<Status> timeline = null;
        try { timeline = yamba.getTimeline(20); }
        catch (YambaClientException e) {
            Log.e(TAG, "Poll failed: ", e);
        }

        processTimeline(timeline);
    }

    private void processTimeline(List<Status> timeline) {
        for (Status status : timeline) {
            Log.d(TAG, "Status: "  + status.getId());
            Log.d(TAG, "    time: "  + status.getCreatedAt());
            Log.d(TAG, "    user: "  + status.getUser());
            Log.d(TAG, "    message: "  + status.getMessage());
        }
    }
}
