package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrayerWorker extends AsyncTask<Object, Void, String> {
    public PrayerEvent caller;

    @Override
    protected String doInBackground(Object... params) {
        caller = (PrayerEvent) params[2];
        String urlAddress = (String) params[0];
        String date = (String) params[1];

        InputStream in = null;

        try {
            URL url = new URL(urlAddress + "photos?date=" + date);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream());

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // create the prayer
        caller.apiCallFinished(new Prayer());
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
