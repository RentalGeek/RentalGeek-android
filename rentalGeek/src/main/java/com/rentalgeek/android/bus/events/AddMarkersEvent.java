package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.model.RentalMarker;

import java.util.List;

/**
 * Created by Alan R on 10/20/15.
 */
public class AddMarkersEvent {
    List<RentalMarker> markers;

    public AddMarkersEvent(List<RentalMarker> markers) {
        this.markers = markers;
    }

    public List<RentalMarker> getMarkers() {
        return markers;
    }
}
