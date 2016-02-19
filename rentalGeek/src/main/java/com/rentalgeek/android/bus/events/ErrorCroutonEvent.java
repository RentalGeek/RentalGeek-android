package com.rentalgeek.android.bus.events;

public class ErrorCroutonEvent {

    private String message;

    public ErrorCroutonEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
