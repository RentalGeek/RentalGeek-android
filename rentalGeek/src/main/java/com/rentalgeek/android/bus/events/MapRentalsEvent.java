package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.MapRental;

import java.util.ArrayList;

public class MapRentalsEvent {

    private ArrayList<MapRental> mapRentals;

    public MapRentalsEvent(ArrayList<MapRental> mapRentals) {
        this.mapRentals = mapRentals;
    }

    public ArrayList<MapRental> getMapRentals() {
        return mapRentals;
    }

}
