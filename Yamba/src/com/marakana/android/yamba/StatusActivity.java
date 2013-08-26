package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

public class StatusActivity extends Activity {
    private static final String TAG = "STATUS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }
        setContentView(R.layout.activity_status);
    }

    @Override
    protected void onDestroy() {
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onDestroy"); }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onPause"); }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onResume"); }
        super.onResume();
    }

    @Override
    protected void onStart() {
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onStart"); }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onStop"); }
        super.onStop();
    }

}
