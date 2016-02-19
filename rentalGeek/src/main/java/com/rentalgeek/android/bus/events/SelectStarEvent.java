package com.rentalgeek.android.bus.events;

public class SelectStarEvent {

    private int position;

    public SelectStarEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
