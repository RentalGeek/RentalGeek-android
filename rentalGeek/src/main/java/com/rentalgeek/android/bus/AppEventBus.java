package com.rentalgeek.android.bus;


import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.logging.AppLogger;

public class AppEventBus {

    private static final String TAG = "AppEventBus";

    public static void postUserNotification(String message) {
        try {
            RentalGeekApplication.postUserNotification(message);
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    public static void postUserNotification(int resId) {
        try {
            RentalGeekApplication.postUserNotification(resId);
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    public static void post(Object event) {
        RentalGeekApplication.eventBus.post(event);
    }

    public static void postSticky(Object event) {
        RentalGeekApplication.eventBus.postSticky(event);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getStickyEvent(Class<T> clazz) {
        Object eventObject = RentalGeekApplication.eventBus.getStickyEvent(clazz);
        if (eventObject == null) return null;
        else return (T) eventObject;
    }

    @SuppressWarnings("unchecked")
    public static <T> T removeStickyEvent(Class<T> clazz) {
        Object eventObject = RentalGeekApplication.eventBus.removeStickyEvent(clazz);
        if (eventObject == null) return null;
        else return (T) eventObject;
    }

    public static void removeAllStickyEvents() {
        RentalGeekApplication.eventBus.removeAllStickyEvents();
    }

//    public static EventType getSyncStatusEvent() {
//        Object eventObject = RentalGeekApplication.eventBus.getStickyEvent(SyncStatusEvent.class);
//        if (eventObject == null) return EventType.Idle;
//        else {
//            SyncStatusEvent event = (SyncStatusEvent) eventObject;
//            return event.getEventType();
//        }
//    }

    public static void register(Object subscriber) {
        try {
            RentalGeekApplication.eventBus.register(subscriber);
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
