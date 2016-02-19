package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.Rental;

public class ShowRentalEvent {

    private Rental rental;

    public ShowRentalEvent(Rental rental) {
        this.rental = rental;
    }

    public Rental getRental() {
        return this.rental;
    }

}
