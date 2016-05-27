package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.MapRental;

import java.util.ArrayList;

public interface MapView {

    void zoomTo(double latitude, double longitude, int zoom);
    void setRentals(ArrayList<MapRental> mapRentals);
    void boundbox();

}
