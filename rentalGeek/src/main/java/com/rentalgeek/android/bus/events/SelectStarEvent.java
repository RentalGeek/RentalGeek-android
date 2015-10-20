package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R 10/20/15.
 */
public class SelectStarEvent {

    private int position;

    public SelectStarEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
