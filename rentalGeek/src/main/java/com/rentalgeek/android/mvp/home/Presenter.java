package com.rentalgeek.android.mvp.home;

import android.os.Bundle;

public interface Presenter {

    void getMapRentalOfferings();
    void getMapRentalOfferings(String location);
    void getListRentalOfferings();
    void getListRentalOfferings(String location);
    void getRentalOfferings(Bundle bundle);

}
