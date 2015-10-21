package com.rentalgeek.android.api;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Alan R on 10/21/15.
 */
public interface LoginInterface  {

    void clicked(Activity context);
    void setup(Activity context);
    void onStart(Activity context);
    void onStop(Activity context);
    void onActivityResult(Activity context,int requestCode, int resultCode, Intent data);
    void setValidation(Object controller);
}
