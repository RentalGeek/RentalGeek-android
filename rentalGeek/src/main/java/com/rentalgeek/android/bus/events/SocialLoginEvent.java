package com.rentalgeek.android.bus.events;

import android.os.Bundle;

/**
 * Created by Alan R on 10/21/15.
 */
public class SocialLoginEvent {

    private Bundle bundle;

    public SocialLoginEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return this.bundle;
    }
}
