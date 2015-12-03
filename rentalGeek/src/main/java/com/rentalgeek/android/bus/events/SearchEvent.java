package com.rentalgeek.android.bus.events;

import android.os.Bundle;

public class SearchEvent {
    private Bundle bundle;

    public SearchEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}

