package com.rentalgeek.android.bus;


import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.logging.AppLogger;

public class AppEventBus {

    private static final String TAG = "AppEventBus";

    public static void post(Object event) {
        RentalGeekApplication.eventBus.post(event);
    }

    public static void register(Object subscriber) {
        try {
            if (!RentalGeekApplication.eventBus.isRegistered(subscriber)) {
                RentalGeekApplication.eventBus.register(subscriber);
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    public static void unregister(Object subscriber) {
        try {
            RentalGeekApplication.eventBus.unregister(subscriber);
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

}
