package com.rentalgeek.android.mvp.list.rental;

import com.rentalgeek.android.pojos.Rental;

public interface RentalListView {
    public void setRentals(Rental[] rentals);

    public void removeItem(int position);
}
