package com.rentalgeek.android.mvp.home;

import com.rentalgeek.android.constants.ManhattanKansas;
import com.rentalgeek.android.pojos.ListRentalsManager;
import com.rentalgeek.android.pojos.MapRentalsManager;

public class HomePresenter implements Presenter {

    private ManhattanKansas manhattanKansas;

    public HomePresenter(ManhattanKansas manhattanKansas) {
        this.manhattanKansas = manhattanKansas;
    }

    @Override
    public void getMapRentalOfferings() {
        MapRentalsManager.getInstance().getAll();
    }

    @Override
    public void getListRentalOfferings() {
        ListRentalsManager.getInstance().getAll();
    }

}
