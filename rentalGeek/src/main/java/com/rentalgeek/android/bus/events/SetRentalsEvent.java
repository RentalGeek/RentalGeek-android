package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.Rental;

public class SetRentalsEvent {

    private Rental[] rentals;

    public SetRentalsEvent(Rental[] rentals) {
        this.rentals = rentals;
    }

    public Rental[] getRentals() {
        return this.rentals;
    }

}
