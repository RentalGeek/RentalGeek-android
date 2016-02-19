package com.rentalgeek.android.bus.events;

public class ShowMessageAlert extends BaseAlertEvent {

    public ShowMessageAlert(String title, String message) {
        super(title, message);
    }

}
