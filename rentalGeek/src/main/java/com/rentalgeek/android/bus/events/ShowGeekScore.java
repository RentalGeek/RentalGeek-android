package com.rentalgeek.android.bus.events;

public class ShowGeekScore {

    private String score;

    public ShowGeekScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return this.score;
    }

}
