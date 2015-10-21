package com.rentalgeek.android.ui.activity;

import android.content.Intent;

/**
 * Created by Alan R on 10/21/15.
 */
public class LoginResult {

    private Intent data;
    private int resultCode;
    private int requestCode;

    public LoginResult(Intent data, int resultCode, int requestCode) {
        this.data = data;
        this.resultCode = resultCode;
        this.requestCode = requestCode;
    }

    public Intent getData() {
        return this.data;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public int getRequestCode() {
        return this.requestCode;
    }
}
