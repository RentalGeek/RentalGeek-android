package com.rentalgeek.android.mvp.fav;

import android.util.Log;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

import org.json.JSONArray;
import org.json.JSONObject;


public class FavPresenter implements Presenter {
    
    private static final String TAG = FavPresenter.class.getSimpleName();

    private FavView favView;

    public FavPresenter(FavView favView) {
        this.favView = favView;
    }

    @Override
    public void getFavoriteRentals() {
        String url = ApiManager.getPropertySearchUrl("");
        String token = AppPreferences.getAuthToken();
        
        url = String.format("%s?starred=true",url);

        GlobalFunctions.getApiCall(null,url,token,new GeekHttpResponseHandler() {
            @Override public void onStart() {}

            @Override public void onFinish() {}

            @Override public void onSuccess(String response) {
                    try {
                        JSONObject json = new JSONObject(response);
                        JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                        Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(),Rental[].class);
                        
                        if( rentals != null && rentals.length > 0 ) {
                            favView.setRentals(rentals);
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
