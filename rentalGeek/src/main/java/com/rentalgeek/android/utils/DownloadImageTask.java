package com.rentalgeek.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageLayout;

    public DownloadImageTask(ImageView imageLayout) {
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
        int originalHeight = imageLayout.getHeight();
        Drawable d = new BitmapDrawable(bitmap);
        d.setAlpha(150);
        imageLayout.setMaxHeight(originalHeight);
        imageLayout.setImageDrawable(d);
    }

}
