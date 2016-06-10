package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.ListRental;

import java.util.ArrayList;

public class ListRentalsEvent {

    private ArrayList<ListRental> listRentals;

    public ListRentalsEvent(ArrayList<ListRental> listRentals) {
        this.listRentals = listRentals;
    }

    public ArrayList<ListRental> getListRentals() {
        return listRentals;
    }

}
