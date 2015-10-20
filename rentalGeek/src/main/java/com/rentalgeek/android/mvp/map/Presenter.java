package com.rentalgeek.android.mvp.map;

import com.rentalgeek.android.pojos.Rental;

public interface Presenter {
    public void addRentals(Rental[] rentals);

    public void getRental(String rental_id);
}
