package com.rentalgeek.android.bus.events;

public class UnSelectStarEvent {

    private int position;

    public UnSelectStarEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
