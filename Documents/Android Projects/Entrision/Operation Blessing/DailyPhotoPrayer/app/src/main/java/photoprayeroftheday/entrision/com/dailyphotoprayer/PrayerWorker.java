package photoprayeroftheday.entrision.com.dailyphotoprayer;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
            Bitmap image = null;

            while((line = reader.readLine()) != null) {
                JSONArray json = new JSONArray(line);

                JSONObject object = (JSONObject) json.get(0);
                JSONObject photoURLObject = (JSONObject) object.getJSONObject("url");
                photoURL = photoURLObject.getString("url");
                location = object.getString("location");
                prayerText = object.getString("prayer");
            }

            if (photoURL != null) {
                URL imageURL = new URL(photoURL);
                HttpURLConnection imageConn = (HttpURLConnection) imageURL.openConnection();
                imageConn.setDoInput(true);
                imageConn.connect();

                InputStream is = conn.getInputStream();
                image = BitmapFactory.decodeStream(is);

                ContextWrapper cw = new ContextWrapper(caller.caller);
                FileOutputStream file = cw.openFileOutput (date + ".png", Context.MODE_PRIVATE);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                file.write(stream.toByteArray());
                file.close();
            }

            if (prayerText != null) {
                Prayer prayer = new Prayer();
                prayer.setValues(date, location, prayerText);
                return prayer;
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
