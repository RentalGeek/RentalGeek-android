package com.rentalgeek.android.mvp.home;

import com.rentalgeek.android.pojos.ListRentalsManager;
import com.rentalgeek.android.pojos.MapRentalsManager;

public class HomePresenter implements Presenter {

    @Override
    public void getMapRentalOfferings() {
        MapRentalsManager.getInstance().getAll();
    }

    @Override
    public void getListRentalOfferings() {
        ListRentalsManager.getInstance().getAll();
    }

}
