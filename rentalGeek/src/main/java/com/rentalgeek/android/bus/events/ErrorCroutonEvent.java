package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R on 10/21/15.
 */
public class ErrorCroutonEvent {
    private String message;

    public ErrorCroutonEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
