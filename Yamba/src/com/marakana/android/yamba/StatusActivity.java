package com.marakana.android.yamba;

import android.os.Bundle;
import android.util.Log;

public class StatusActivity extends YambaActivity  {
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
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        if (BuildConfig.DEBUG) { Log.d(TAG, "called onCreate: " + this); }

        setContentView(R.layout.activity_status);
    }
}
