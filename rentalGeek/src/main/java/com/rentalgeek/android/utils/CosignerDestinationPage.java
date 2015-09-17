package com.rentalgeek.android.utils;

/**
 * Created by rajohns on 9/17/15.
 *
 */
public enum CosignerDestinationPage {

    INSTANCE;

    private String destination = "";

    public static CosignerDestinationPage getInstance() {
        return INSTANCE;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
