package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.app.Activity;

public class PrayerEvent {

    private PrayerDataSource dataSource;
    private APIPrayer api;
    private boolean apiCallFinished;
    private Activity caller;

    public PrayerEvent (APIPrayer prayer, Activity parent) {
        api = prayer;
        apiCallFinished = false;
        caller = parent;
    }

    public void getPrayerForDate(String date) {
        dataSource = new PrayerDataSource(caller);
        dataSource.open();

        // check the DB for the prayer
        Prayer prayer;

        // check the database for the prayer
        prayer = dataSource.getPrayerForDate(date);

        dataSource.close();

        // check the API for the prayer
        if (prayer == null) {
            // call the API to get prayers
            new PrayerWorker().execute(caller.getString(R.string.baseAddress), date, this);
        } else {
            api.apiCallFinished(prayer);
        }
/*
        // check the API for prayer
        if(apiCallFinished) {
            api.apiCallFinished();
        }
        */
    }

    public void apiCallFinished(Prayer prayer) {
        api.apiCallFinished(prayer);
    }
}
