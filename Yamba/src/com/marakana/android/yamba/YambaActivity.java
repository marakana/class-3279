package com.marakana.android.yamba;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class YambaActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_status:
                nextPage(StatusActivity.class);
                break;

            case R.id.menu_timeline:
                nextPage(TimelineActivity.class);
                break;

            case R.id.menu_prefs:
                Intent i = new Intent(this, YambaPrefs.class);
                i.putExtra(
                        ":android:show_fragment",
                        "com.marakana.android.yamba.YambaPrefs$ClientPrefs");
                startActivity(i);
                break;

            case R.id.menu_about:
                Toast.makeText(this, R.string.about, Toast.LENGTH_LONG).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void nextPage(Class<?> klass) {
        Intent i = new Intent(this, klass);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
