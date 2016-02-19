package com.rentalgeek.android.bus.events;


public class UserNotificationEvent {

    private String message = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserNotificationEvent(String message) {
        this.setMessage(message);
    }

}
