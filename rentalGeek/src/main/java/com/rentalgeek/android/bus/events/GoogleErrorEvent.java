package com.rentalgeek.android.bus.events;

import com.google.android.gms.common.ConnectionResult;

public class GoogleErrorEvent {

    private ConnectionResult connectionResult;

    public GoogleErrorEvent(ConnectionResult connectionResult) {
        this.connectionResult = connectionResult;
    }

    public ConnectionResult getConnectionResult() {
        return this.connectionResult;
    }

}
