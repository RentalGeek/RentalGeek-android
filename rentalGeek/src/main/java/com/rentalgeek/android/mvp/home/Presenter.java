package com.rentalgeek.android.mvp.home;

import android.os.Bundle;

public interface Presenter {

    void getRentalOfferings(String location);
    void getRentalOfferings(Bundle bundle);

}
