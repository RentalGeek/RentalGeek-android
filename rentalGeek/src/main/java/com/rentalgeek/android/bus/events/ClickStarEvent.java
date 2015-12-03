package com.rentalgeek.android.bus.events;

import android.os.Bundle;

public class ClickStarEvent {
    private Bundle bundle;

    public ClickStarEvent(Bundle bundle) {

        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}

