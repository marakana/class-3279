
package com.marakana.android.yamba;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

public class TimelineActivity extends ListActivity implements LoaderCallbacks<Cursor> {
    private static final int TIMELINE_LOADER = 666;

    private static final String[] PROJ = new String[] {
        YambaContract.Timeline.Columns.ID,
        YambaContract.Timeline.Columns.USER,
        YambaContract.Timeline.Columns.CREATED_AT,
        YambaContract.Timeline.Columns.STATUS,
    };

    private static final String[] FROM = new String[PROJ.length - 1];
    static { System.arraycopy(PROJ, 1, FROM, 0, FROM.length); }

    private static final int[] TO = new int[] {
        R.id.timeline_row_user,
        R.id.timeline_row_time,
        R.id.timeline_row_status,
    };

    static class TimelineBinder implements SimpleCursorAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View v, Cursor c, int col) {
            if (R.id.timeline_row_time != v.getId()) { return false; }

            CharSequence s = "long ago";
            long t = c.getLong(col);
            if (0 < t) { s = DateUtils.getRelativeTimeSpanString(t); }
            ((TextView) v).setText(s);

            return true;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                YambaContract.Timeline.URI,
                PROJ,
                null,
                null,
                YambaContract.Timeline.Columns.CREATED_AT + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {
        // See: http://code.google.com/p/android/issues/detail?id=42945
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(cur);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((SimpleCursorAdapter) getListAdapter()).swapCursor(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setBackgroundResource(R.drawable.background);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.timeline_row,
                null,
                FROM,
                TO,
                0);
        setListAdapter(adapter);
        adapter.setViewBinder(new TimelineBinder());

        getLoaderManager().initLoader(TIMELINE_LOADER, null, this);
    }
}
