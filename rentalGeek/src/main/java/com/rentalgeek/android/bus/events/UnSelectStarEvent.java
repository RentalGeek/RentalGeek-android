package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R on 10/20/15.
 */
public class UnSelectStarEvent {

    private int position;

    public UnSelectStarEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
