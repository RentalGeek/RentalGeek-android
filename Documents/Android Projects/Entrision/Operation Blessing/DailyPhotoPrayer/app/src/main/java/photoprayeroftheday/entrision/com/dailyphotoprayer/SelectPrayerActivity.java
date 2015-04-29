package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class SelectPrayerActivity extends Activity implements APIPrayer {

    private PrayerDataSource dataSource;
    private int foundPrayers;
    private int dayCount;
    private boolean prayerSearchFinished;
    private PrayerEvent prayerEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_prayer);

        foundPrayers = 0;
        dayCount = 0;
        prayerSearchFinished = false;
        prayerEvent = new PrayerEvent(this, this);

        //dataSource = new PrayerDataSource(this);
        //dataSource.open();

        // get today's date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());

        prayerEvent.getPrayerForDate(date);
/*
        // cycle through dates until we have 7 or have gone back 30 days
        for (int i=0; i < 29; i++) {
            Prayer prayer;
            String date = dateFormat.format(calendar.getTime());

            // check the database for the prayer
            prayer = dataSource.getPrayerForDate(date);

            // check the API for the prayer
            if (prayer == null) {
                // call the API to get prayers
                new PrayerWorker().execute(this.getString(R.string.baseAddress), date);
            }

            // load into view

            // set the current day
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
*/

        // click handlers
        View todayImage = findViewById(R.id.todayImage);

        todayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectPrayerActivity.this, SinglePrayerActivity.class);

                // TODO -  add in intent extra
                SelectPrayerActivity.this.startActivity(intent);
            }
        });
    }

    public void apiCallFinished(Prayer foundPrayer) {
        // check to see if there is a prayer

        // if so add it to the array

        // if we have not met the terminate criteria:

        // change the date

        // make call again
    }

}
