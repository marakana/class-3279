
package com.marakana.android.yamba;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TimelineActivity extends YambaActivity {
    private static final String TAG = "TIMEACT";

    private static final String TAG_DETAILS = "TimelineActivity.DETAILS";


    private boolean usingFragments;

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (usingFragments) { launchDetailFragment(intent.getExtras()); }
        else { startActivity(intent); }
    }

    public void launchDetailFragment(Bundle args) {
        FragmentTransaction xact = getFragmentManager().beginTransaction();

        xact.replace(
                R.id.timeline_detail,
                TimelineDetailFragment.newInstance(args),
                TAG_DETAILS);
        xact.addToBackStack(null);
        xact.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        xact.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }

        setContentView(R.layout.activity_timeline);

        usingFragments = null != findViewById(R.id.timeline_detail);

        if (usingFragments) { addDetailFragment(); }
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

    private void addDetailFragment() {
        FragmentManager mgr = getFragmentManager();

        if (null != mgr.findFragmentByTag(TAG_DETAILS)) { return; }

        FragmentTransaction xact = mgr.beginTransaction();
        xact.add(
                R.id.timeline_detail,
                TimelineDetailFragment.newInstance(),
                TAG_DETAILS);
        xact.commit();
    }
}
