package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.ListRental;

import java.util.ArrayList;

public class FavoriteRentalsEvent {

    private ArrayList<ListRental> favoriteRentals;

    public FavoriteRentalsEvent(ArrayList<ListRental> favoriteRentals) {
        this.favoriteRentals = favoriteRentals;
    }

    public ArrayList<ListRental> getFavoriteRentals() {
        return favoriteRentals;
    }

}
