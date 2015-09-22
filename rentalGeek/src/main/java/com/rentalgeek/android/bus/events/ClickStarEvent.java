package com.rentalgeek.android.bus.events;

import android.os.Bundle;

import com.rentalgeek.android.mvp.common.StarView;

public class ClickStarEvent {
    private Bundle bundle;
    private StarView starView;

    public ClickStarEvent(Bundle bundle, StarView starView) {
        this.bundle = bundle;
        this.starView = starView;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public StarView getStarView() {
        return starView;
    }
}

