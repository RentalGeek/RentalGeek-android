package com.rentalgeek.android.bus.events;

/**
 * Created by alan on 10/20/15.
 */
abstract class BaseAlertEvent {

    private String title;
    private String message;

    public BaseAlertEvent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
