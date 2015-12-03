package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R on 10/20/15.
 */
public class ShowMessageAlert extends BaseAlertEvent {

    public ShowMessageAlert(String title, String message) {
        super(title, message);
    }
}
