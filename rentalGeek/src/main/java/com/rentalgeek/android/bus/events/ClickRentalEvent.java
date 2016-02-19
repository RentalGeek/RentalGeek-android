package com.rentalgeek.android.bus.events;

import android.os.Bundle;

public class ClickRentalEvent {

    private Bundle bundle;

    public ClickRentalEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

}

