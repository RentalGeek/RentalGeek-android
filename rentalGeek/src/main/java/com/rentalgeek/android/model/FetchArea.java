package com.rentalgeek.android.model;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.constants.Distance;

public class FetchArea {

    public LatLng centerPoint;
    public int radiusInMiles;

    public FetchArea(GoogleMap map) {
        this.centerPoint = map.getCameraPosition().target;
        LatLng southWestPoint = map.getProjection().getVisibleRegion().latLngBounds.southwest;
        LatLng northeastPoint = map.getProjection().getVisibleRegion().latLngBounds.northeast;
        float[] distance = new float[1];
        Location.distanceBetween(southWestPoint.latitude, southWestPoint.longitude, northeastPoint.latitude, northeastPoint.longitude, distance);
        int calculatedRadius = (int)(distance[0] / Distance.METERS_IN_MILE) / 2;
        this.radiusInMiles = calculatedRadius > 0 ? calculatedRadius : 1;
    }

}
