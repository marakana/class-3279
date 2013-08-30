
package com.marakana.android.yamba;

import android.os.Bundle;
import android.util.Log;

public class TimelineActivity extends YambaActivity {
    private static final String TAG = "TIMEACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }

        setContentView(R.layout.activity_timeline);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //YambaService.stopPolling(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //YambaService.startPolling(this);
    }
}
