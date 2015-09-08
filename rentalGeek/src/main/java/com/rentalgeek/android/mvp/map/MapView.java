package com.rentalgeek.android.mvp.map;

import com.google.android.gms.maps.model.MarkerOptions;

public interface MapView {
    public void addMarker(MarkerOptions marker);
    public void zoomTo(double latitude, double longitude, int zoom);
}
