package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.model.RentalMarker;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapView {
    public void addMarker(RentalMarker marker);
    public void zoomTo(double latitude, double longitude, int zoom);
    public void setRentals(Rental[] rentals);
    public void boundbox();
    public void setRental(Rental rental);
}
