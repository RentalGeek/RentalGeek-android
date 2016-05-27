package com.rentalgeek.android.model;

import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.pojos.MapRental;

public class RentalMarker {

    private MapRental rental;
    private MarkerOptions marker;

    public void setRental(MapRental rental) {
        this.rental = rental;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public MapRental getRental() {
        return rental;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

}
