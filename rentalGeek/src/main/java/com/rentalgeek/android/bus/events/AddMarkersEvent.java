package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.model.RentalMarker;

import java.util.List;

public class AddMarkersEvent {

    List<RentalMarker> markers;

    public AddMarkersEvent(List<RentalMarker> markers) {
        this.markers = markers;
    }

    public List<RentalMarker> getMarkers() {
        return markers;
    }

}
