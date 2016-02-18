package com.rentalgeek.android.bus.events;

public class ShowProgressEvent {

    private int msgId;

    public ShowProgressEvent(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgId() {
        return msgId;
    }

}

