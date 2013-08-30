package com.marakana.android.yamba;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class StatusActivity extends Activity  {
    private static final String TAG = "STATUSACT";

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

        setContentView(R.layout.activity_status);
    }
}
