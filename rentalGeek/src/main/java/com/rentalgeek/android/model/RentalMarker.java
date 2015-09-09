package com.rentalgeek.android.model;

import com.rentalgeek.android.pojos.Rental;
import com.google.android.gms.maps.model.MarkerOptions;

//Since Google doesnt let me extend MarkerOptions
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
