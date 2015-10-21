package com.rentalgeek.android.api;

import android.app.Activity;

/**
 * Created by Alan R on 10/21/15.
 */
public interface LoginInterface  {

    void clicked();
    void setup(Activity context);
    void onStart();
    void onStop();
    void onActivityResult(int requestCode, int resultCode);
}
