package com.luttu.gps;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GpsReadingService extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	private LocationRequest mLocationRequest;
	private GoogleApiClient mGoogleApiClient;
	//private LocationClient mLocationClient
	public static LocationUpdate locationUpdate;
	private final String TAG = "RentalGEEK";

	/*
	 * Called befor service onStart method is called.All Initialization part
	 * goes here
	 */
	@Override
	public void onCreate() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		/*
		mLocationRequest = LocationRequest.create();
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(getApplicationContext(), this,
				this);
		mLocationClient.connect();
		*/
	}

	/*
	 * You need to write the code to be executed on service start. Sometime due
	 * to memory congestion DVM kill the running service but it can be restarted
	 * when the memory is enough to run the service again.
	 */

	@Override
	public void onStart(Intent intent, int startId) {
		int start = Service.START_STICKY;
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		// Disconnecting the client invalidates it.
		mGoogleApiClient.disconnect();
		//super.onStop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * Overriden method of the interface
	 * GooglePlayServicesClient.OnConnectionFailedListener . called when
	 * connection to the Google Play Service are not able to connect
	 */

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			// try {
			// Start an Activity that tries to resolve the error
			// connectionResult.startResolutionForResult(this,
			// LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
			/*
			 * Thrown if Google Play services canceled the original
			 * PendingIntent
			 */
			// } catch (IntentSender.SendIntentException e) {
			// // Log the error
			// e.printStackTrace();
			// }
		} else {
			// If no resolution is available, display a dialog to the user with
			// the error.
			Log.i("info", "No resolution is available");
		}
	}

	/*
	 * This is overriden method of interface
	 * GooglePlayServicesClient.ConnectionCallbacks which is called when
	 * locationClient is connecte to google service. You can receive GPS reading
	 * only when this method is called.So request for location updates from this
	 * method rather than onStart()
	 */
	@Override
	public void onConnected(Bundle arg0) {
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(1000); // Update location every second

		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}


	/*
	 * Overrriden method of interface LocationListener called when location of
	 * gps device is changed. Location Object is received as a parameter. This
	 * method is called when location of GPS device is changed
	 */
	@Override
	public void onLocationChanged(Location location) {
		if (locationUpdate != null)
			locationUpdate.locationupdate(location);
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean isServicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(GpsReadingService.this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {

			return false;
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "GoogleApiClient connection has been suspend");
	}

	/*
	 * Called when Sevice running in backgroung is stopped. Remove location
	 * upadate to stop receiving gps reading
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("info", "Service is destroyed");
		//mLocationClient.removeLocationUpdates(this);
		super.onDestroy();
	}

	public interface LocationUpdate {
		void locationupdate(Location location);
	}
}
