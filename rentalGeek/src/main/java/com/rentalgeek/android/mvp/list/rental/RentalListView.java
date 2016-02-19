package com.rentalgeek.android.mvp.list.rental;

import com.rentalgeek.android.pojos.Rental;

public interface RentalListView {

    void setRentals(Rental[] rentals);
    void removeItem(int position);

}
