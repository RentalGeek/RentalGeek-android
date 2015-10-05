package com.rentalgeek.android.mvp.home;

import com.rentalgeek.android.mvp.common.BaseView;
import com.rentalgeek.android.pojos.Rental;

public interface HomeView extends BaseView {
    void setRentals(Rental[] rentals);
}
