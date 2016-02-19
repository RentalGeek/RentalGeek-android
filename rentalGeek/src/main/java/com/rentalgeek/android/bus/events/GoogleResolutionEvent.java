package com.rentalgeek.android.bus.events;

import com.google.android.gms.common.ConnectionResult;

public class GoogleResolutionEvent {

    private ConnectionResult connectionResult;

    public GoogleResolutionEvent(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }

    public ConnectionResult getConnectionResult() {
        return this.connectionResult;
    }

}

