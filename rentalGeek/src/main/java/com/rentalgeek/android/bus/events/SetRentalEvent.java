package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.Rental;

/**
 * Created by Alan R on 10/20/15.
 */
public class SetRentalEvent {
    private Rental rental;

    public SetRentalEvent(Rental rental) {
        this.rental = rental;
    }

    public Rental getRental() {
        return rental;
    }
}
