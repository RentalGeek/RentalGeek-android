package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.Rental;

/**
 * Created by Alan R on 10/20/15.
 */
public class SetRentalsEvent {
    private Rental[] rentals;

    public SetRentalsEvent(Rental[] rentals) {
        this.rentals = rentals;
    }

    public Rental[] getRentals() {
        return this.rentals;
    }
}
