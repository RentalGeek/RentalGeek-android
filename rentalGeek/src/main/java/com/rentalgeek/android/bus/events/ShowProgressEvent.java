package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R on 10/21/15.
 */
public class ShowProgressEvent {
    private int msgId;

    public ShowProgressEvent(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgId() {
        return msgId;
    }
}

