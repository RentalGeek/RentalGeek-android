package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.MapRental;

import java.util.ArrayList;

public interface Presenter {

    void addRentals(ArrayList<MapRental> rentals);
    void getRental(String rental_id);

}
