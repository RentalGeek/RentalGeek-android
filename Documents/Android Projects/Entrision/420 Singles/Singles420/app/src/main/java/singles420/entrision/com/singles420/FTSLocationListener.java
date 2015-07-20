package singles420.entrision.com.singles420;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by travis on 7/20/15.
 */
public class FTSLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {

        String longitude = String.valueOf(loc.getLongitude());
        String latitude = String.valueOf(loc.getLatitude());

        APIHelper.getInstance().updateUserLocation(latitude, longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider,
                                int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
