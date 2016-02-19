package com.rentalgeek.android.bus.events;

import android.os.Bundle;

public class SocialLoginEvent {

    private Bundle bundle;

    public SocialLoginEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

}
