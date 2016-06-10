package com.rentalgeek.android.mvp.map;

import android.content.Context;

import com.rentalgeek.android.pojos.MapRental;

import java.util.ArrayList;

public interface Presenter {

    void addRentals(Context context, ArrayList<MapRental> rentals);
    void getRental(String rental_id);

}
