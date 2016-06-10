package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.RentalDetail;

public class RentalDetailEvent {

    private RentalDetail rentalDetail;

    public RentalDetailEvent(RentalDetail rentalDetail) {
        this.rentalDetail = rentalDetail;
    }

    public RentalDetail getRentalDetail() {
        return this.rentalDetail;
    }

}
