package com.rentalgeek.android.mvp.home;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import com.rentalgeek.android.api.ApiManager;

import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;

import com.rentalgeek.android.pojos.Rental;

import com.rentalgeek.android.ui.preference.AppPreferences;

import com.rentalgeek.android.utils.GeekGson;

import com.rentalgeek.android.storage.RentalCache;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomePresenter implements Presenter {
    
    private static final String TAG = HomePresenter.class.getSimpleName();

    private HomeView homeView;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override public void getRentalOfferings() {
        String url = ApiManager.getPropertySearchUrl();
        String token = AppPreferences.getAuthToken();
            
        GlobalFunctions.getApiCall(null,url,token,new GeekHttpResponseHandler() {
            @Override public void onStart() {}

            @Override public void onFinish() {}

            @Override public void onSuccess(String response) {
                    
                try {
                        JSONObject json = new JSONObject(response);
                        JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                        Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(),Rental[].class);
                        
                        if( rentals != null && rentals.length > 0 ) {
 
                            System.out.println(String.format("Found %d rentals based on query.",rentals.length));

                            for(Rental rental : rentals) {
                                RentalCache.getInstance().add(rental);
                            }

                            homeView.setRentals(rentals);
                        }
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }

            @Override public void onAuthenticationFailed() {}
        });
    }
}
