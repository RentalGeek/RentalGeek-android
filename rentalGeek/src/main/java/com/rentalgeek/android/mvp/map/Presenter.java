package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.Rental;

public interface Presenter {

    void addRentals(Rental[] rentals);
    void getRental(String rental_id);

}
