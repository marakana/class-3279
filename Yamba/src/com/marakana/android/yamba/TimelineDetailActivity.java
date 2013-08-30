package com.marakana.android.yamba;

import android.os.Bundle;
import android.util.Log;

public class TimelineDetailActivity extends YambaActivity  {
    private static final String TAG = "DETAILACT";

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }

        setContentView(R.layout.activity_timeline_detail);

        TimelineDetailFragment frag
            = (TimelineDetailFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_details);
        frag.setDetails(getIntent().getExtras());
    }
}
