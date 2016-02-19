package com.rentalgeek.android.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.receiver.GcmBroadcastReceiver;

public class GcmIntentService extends NonStopIntentService {

    public GcmIntentService(String name) {
        super(name);
    }

    protected static final String TAG = "GcmIntentService";
    private GoogleCloudMessaging gcm;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle extras = intent != null ? intent.getExtras() : null;
                gcm = GoogleCloudMessaging.getInstance(this);
                if (gcm == null) return;
                String messageType = gcm.getMessageType(intent);
                AppLogger.log(TAG, "onhandle gcm:" + messageType);
                if (extras != null && !extras.isEmpty()) {
                    if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                        String message = extras.getString("message");
                        if (!TextUtils.isEmpty(message)) {
                        }
                    }
                }
            } catch (Exception e) {
                AppLogger.log(TAG, e);
            } finally {
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }

    }

    @SuppressWarnings("incomplete-switch")
    private void onGcmMessageReceived(Message message) {
        if (message == null) return;
        try {
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

}
