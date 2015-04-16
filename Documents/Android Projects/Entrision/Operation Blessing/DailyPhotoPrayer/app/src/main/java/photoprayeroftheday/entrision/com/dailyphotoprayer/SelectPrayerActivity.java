package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class SelectPrayerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_prayer);

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.header);
*/
        // set up the array
        ArrayList array = new ArrayList();
        array.add("day 1");
        array.add("day 2");
        array.add("day 3");
        array.add("day 4");
        array.add("day 5");
        array.add("day 6");

        // setup the gridview
        GridView gridView = (GridView) findViewById(R.id.grid);

        gridView.setAdapter(new GridViewAdapter(this, array));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getLayoutInflater().inflate(R.layout.header, null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
