package com.rentalgeek.android.mvp.list.rental;

import com.rentalgeek.android.pojos.ListRental;

import java.util.ArrayList;

public interface RentalListView {

    void setRentals(ArrayList<ListRental> listRentals);
    void removeItem(int position);

}
