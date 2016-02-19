package com.rentalgeek.android.bus.events;

public class ErrorAlertEvent extends BaseAlertEvent {

    public ErrorAlertEvent(String title, String message) {
        super(title, message);
    }

}
