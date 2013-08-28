package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.svc.YambaService;

public class StatusActivity extends Activity  {
    private static final String TAG = "STATUS";

//
//    static class Poster extends AsyncTask<String, Void, Integer> {
//        private final Context ctxt;
//
//        public Poster(Context ctxt) { this.ctxt = ctxt; }
//
//        // Runs on daemon thread
//        @Override
//        protected Integer doInBackground(String... status) {
//            YambaClient client = new YambaClient(
//                    "student",
//                    "password",
//                    "http://yamba.marakana.com/api");
//
//            int ret = R.string.post_failed;
//            try {
//                client.postStatus(status[0]);
//                ret = R.string.post_succeeded;
//            }
//            catch (YambaClientException e) {
//                Log.e(TAG, "Post failed", e);
//            }
//
//            return Integer.valueOf(ret);
//        }
//
//        // Runs on UI thread
//        @Override
//        protected void onPostExecute(Integer ret) {
//            Toast.makeText(ctxt, ret.intValue(), Toast.LENGTH_LONG).show();
//            poster = null;
//        }
//    }
//
//    static Poster poster;


    private int maxStatusLen;
    private int warnMax;
    private int errMax;
    private int okColor;
    private int warnColor;
    private int errColor;
    private EditText viewStatus;
    private TextView viewCount;
    private Button buttonSubmit;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prefs:
                startActivity(new Intent(this, YambaPrefs.class));
                break;

            case R.id.menu_about:
                Toast.makeText(this, R.string.about, Toast.LENGTH_LONG).show();
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yamba, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }

        Resources rez = getResources();
        maxStatusLen = rez.getInteger(R.integer.status_max_len);
        warnMax = rez.getInteger(R.integer.warn_max);
        errMax = rez.getInteger(R.integer.err_max);

        okColor = rez.getColor(R.color.status_ok);
        warnColor = rez.getColor(R.color.status_warn);
        errColor = rez.getColor(R.color.status_err);

        setContentView(R.layout.activity_status);

        viewCount = (TextView) findViewById(R.id.status_count);

        buttonSubmit = (Button) findViewById(R.id.status_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { post(); }
        });

        viewStatus = (EditText) findViewById(R.id.status_status);
        viewStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { updateCount(); }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        } );
    }

    void updateCount() {
        int n = viewStatus.getText().length();

        buttonSubmit.setEnabled(checkStatusLen(n));

        n = maxStatusLen - n;

        int c;
        if (n > warnMax) { c = okColor; }
        else if (n > errMax) { c = warnColor; }
        else { c = errColor; }

        viewCount.setText(String.valueOf(n));
        viewCount.setTextColor(c);
    }

    void post() {
//        if (null != poster) { return; }

        String status = viewStatus.getText().toString();
        if (BuildConfig.DEBUG) { Log.d(TAG, "Posting: " + status); }

        if (!checkStatusLen(status.length())) { return; }

        viewStatus.setText("");

        YambaService.post(this, status);

//        poster = new Poster(getApplicationContext());
//        poster.execute(status);
    }

    private boolean checkStatusLen(int n) {
        return (0 < n) && (maxStatusLen >= n);
    }
}
