package com.rentalgeek.android.system;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.preference.AppPreferences;


public enum AppSystem {

    Instance;

    private static final String TAG = "AppSystem";

    private String gcmDeviceId;

    public void checkGCMRegistration(Context context) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

        gcmDeviceId = AppPreferences.getMessageServiceDeviceId();
        if (TextUtils.isEmpty(gcmDeviceId)) {
            registerGCM(gcm);
        }
    }

    private void registerGCM(final GoogleCloudMessaging gcm) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (!TextUtils.isEmpty(result))
                    AppPreferences.setMessageServiceDeviceId(result);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    gcmDeviceId = gcm.register(RentalGeekApplication.GCM_CLIENT_ID);
                    AppLogger.log(TAG, "DEVICE_ID:" + gcmDeviceId);
                } catch (Exception e) {
                    AppLogger.log(TAG, e);
                }
                return gcmDeviceId;
            }

        }.execute(null, null, null);
    }
}
