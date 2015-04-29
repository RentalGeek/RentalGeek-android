package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrayerWorker extends AsyncTask<Object, Void, Prayer> {
    public PrayerEvent caller;

    @Override
    protected Prayer doInBackground(Object... params) {
        caller = (PrayerEvent) params[2];
        String urlAddress = (String) params[0];
        String date = (String) params[1];

        try {
            URL url = new URL(urlAddress + "photos?date=" + date);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            String photoURL = null;
            String location = null;
            String prayerText = null;

            while((line = reader.readLine()) != null) {
                JSONArray json = new JSONArray(line);

                JSONObject object = (JSONObject) json.get(0);
                JSONObject photoURLObject = (JSONObject) object.getJSONObject("url");
                photoURL = photoURLObject.getString("url");
                location = object.getString("location");
                prayerText = object.getString("prayer");
            }

            if (prayerText != null) {

            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Prayer result) {
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
