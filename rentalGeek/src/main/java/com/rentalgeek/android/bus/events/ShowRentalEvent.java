package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.RentalDetail;

public class ShowRentalEvent {

    private RentalDetail rentalDetail;

    public ShowRentalEvent(RentalDetail rentalDetail) {
        this.rentalDetail = rentalDetail;
    }

    public RentalDetail getRental() {
        return this.rentalDetail;
    }

}
