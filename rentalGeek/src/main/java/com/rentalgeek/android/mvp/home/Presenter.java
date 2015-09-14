package com.rentalgeek.android.mvp.home;

import com.google.android.gms.maps.model.LatLng;

public interface Presenter {
    public void getRentalOfferings(LatLng location);
}
