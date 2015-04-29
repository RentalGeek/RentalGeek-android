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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SelectPrayerActivity extends Activity implements APIPrayer {

    private int foundPrayers;
    private int dayCount;
    private boolean prayerSearchFinished;
    private PrayerEvent prayerEvent;
    private ArrayList<Prayer> prayers;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_prayer);

        foundPrayers = 0;
        dayCount = 0;
        prayerSearchFinished = false;
        prayerEvent = new PrayerEvent(this, this);
        prayers = new ArrayList<Prayer>();

        // get today's date
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());

        prayerEvent.getPrayerForDate(date);

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
        if (foundPrayer != null) {
            // add it to the array
            prayers.add(foundPrayers, foundPrayer);
            foundPrayers++;
        }

        dayCount--;

        // if we have not met the terminate criteria:
        if (dayCount > -30 && foundPrayers < 7) {
            // change the date
            // set the current day
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(calendar.getTime());

            // make call again
            prayerEvent.getPrayerForDate(date);
        } else {
            populateUI();
        }
    }

    public void populateUI() {

    }

}
