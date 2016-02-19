package com.rentalgeek.android.mvp.fav;

import android.util.Log;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.NoFavoritesEvent;
import com.rentalgeek.android.bus.events.SetRentalsEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

import org.json.JSONArray;
import org.json.JSONObject;


public class FavPresenter implements Presenter {

    private static final String TAG = FavPresenter.class.getSimpleName();

    @Override
    public void getFavoriteRentals() {
        String url = ApiManager.getFavoritesUrl();
        String token = AppPreferences.getAuthToken();

        System.out.println(url);

        GlobalFunctions.getApiCall(null, url, token, new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                    Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(), Rental[].class);

                    if (rentals != null && rentals.length > 0) {

                        for (Rental rental : rentals) {
                            RentalCache.getInstance().add(rental);
                        }

                        AppEventBus.post(new SetRentalsEvent(rentals));
                    } else {
                        AppEventBus.post(new NoFavoritesEvent());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onAuthenticationFailed() {
            }
        });
    }

}
