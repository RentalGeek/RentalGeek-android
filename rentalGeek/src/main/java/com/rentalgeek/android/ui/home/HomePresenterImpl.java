package com.rentalgeek.android.ui.home;

import android.util.Log;
import org.json.JSONObject;
import org.json.JSONArray;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.utils.GeekGson;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.net.GeekHttpResponseHandler;

public class HomePresenterImpl implements HomePresenter {
    
    private HomeView homeView;

    public HomePresenterImpl(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override public void getRentalOfferings(LatLng location) {
        if( location == null) {
            String url = ApiManager.getPropertySearchUrl("");
            String token = AppPreferences.getAuthToken();
            
            GlobalFunctions.getApiCall(null,url,token,new GeekHttpResponseHandler() {
                @Override public void onStart() {}

                @Override public void onFinish() {}

                @Override public void onSuccess(String response) {
                    
                    try
                    {
                        JSONObject json = new JSONObject(response);
                        JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                        Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(),Rental[].class);
                        
                        if( rentals != null && rentals.length > 0 ) {
                            homeView.setRentals(rentals);
                        }
                    }

                    catch(Exception e) {
                        Log.e("HomePresenterImpl",e.getMessage());
                    }
                }

                @Override public void onAuthenticationFailed() {}
            });
        }
    }
}
