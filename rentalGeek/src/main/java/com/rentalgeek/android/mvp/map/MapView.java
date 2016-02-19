package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.Rental;

public interface MapView {

    void zoomTo(double latitude, double longitude, int zoom);
    void setRentals(Rental[] rentals);
    void boundbox();

}
