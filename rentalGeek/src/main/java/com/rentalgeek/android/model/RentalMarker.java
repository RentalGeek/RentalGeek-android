package com.rentalgeek.android.model;

import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.pojos.Rental;

public class RentalMarker {

    private Rental rental;
    private MarkerOptions marker;

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public Rental getRental() {
        return rental;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

}
