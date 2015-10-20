package com.rentalgeek.android.bus.events;

/**
 * Created by Alan R on 10/20/15.
 */
public class ShowGeekScore {
    private String score;

    public ShowGeekScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return this.score;
    }
}
