package com.rentalgeek.android.utils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.R;

public class MarkerUtils {

    public static MarkerOptions createRentalMarker(LatLng point, int bedroomCount) {

        MarkerOptions marker = new MarkerOptions();
        marker.position(point);

        switch (bedroomCount) {
            case 1:
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.rental_marker_1));
                break;
            case 2:
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.rental_marker_2));
                break;
            case 3:
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.rental_marker_3));
                break;
            default:
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.rental_marker_4_plus));
                break;
        }

        return marker;
    }

}
