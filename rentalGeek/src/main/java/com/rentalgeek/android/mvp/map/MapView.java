package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.Rental;

public interface MapView {
    public void zoomTo(double latitude, double longitude, int zoom);

    public void setRentals(Rental[] rentals);

    public void boundbox();
}
