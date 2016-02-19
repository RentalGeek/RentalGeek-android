package com.rentalgeek.android.bus.events;

public class RemoveItemEvent {

    private int position;

    public RemoveItemEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
