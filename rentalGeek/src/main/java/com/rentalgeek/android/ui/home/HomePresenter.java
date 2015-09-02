package com.rentalgeek.android.ui.home;

import com.google.android.gms.maps.model.LatLng;

public interface HomePresenter {
    public void getRentalOfferings(LatLng location);
}
