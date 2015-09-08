package com.rentalgeek.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.InputStream;

/**
 * Created by rajohns on 9/8/15.
 *
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private LinearLayout imageLayout;

    public DownloadImageTask(LinearLayout imageLayout) {
        this.imageLayout = imageLayout;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imageUrl = params[0];
        Bitmap image = null;

        try {
            InputStream in = new java.net.URL(imageUrl).openStream();
            image = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        int width = imageLayout.getWidth();
        int height = imageLayout.getHeight();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Drawable d = new BitmapDrawable(scaledBitmap);
        d.setAlpha(128);
        imageLayout.setBackground(d);
    }

}
