package com.rentalgeek.android.mvp.home;

import android.os.Bundle;

public interface Presenter {
    public void getRentalOfferings(String location);

    public void getRentalOfferings(Bundle bundle);
}
