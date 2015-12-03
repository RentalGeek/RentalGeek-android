package com.rentalgeek.android.bus.events;


public class UserNotificationEvent {

    private String message = null;
    private int messageId = -1;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public UserNotificationEvent(String message) {
        this.setMessage(message);
    }

    public UserNotificationEvent(int messageId) {
        this.setMessageId(messageId);
    }
}
