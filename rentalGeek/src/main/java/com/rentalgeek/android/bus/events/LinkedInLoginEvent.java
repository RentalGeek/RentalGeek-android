package com.rentalgeek.android.bus.events;

import android.os.Bundle;

/**
 * Created by Alan R on 10/21/15.
 */
public class LinkedInLoginEvent {
    private Bundle bundle;

    public LinkedInLoginEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
