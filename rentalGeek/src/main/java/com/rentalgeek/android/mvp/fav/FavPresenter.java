package com.rentalgeek.android.mvp.fav;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.FavoriteRentalsEvent;
import com.rentalgeek.android.bus.events.NoFavoritesEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.ListRentals;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;


public class FavPresenter implements Presenter {

    @Override
    public void getFavoriteRentals() {
        String url = ApiManager.getFavoritesUrl();
        String token = AppPreferences.getAuthToken();

        System.out.println(url);

        GlobalFunctions.getApiCall(url, token, new GeekHttpResponseHandler() {

            // TODO: MAKE A FAVORITESRENTALMANAGER OR SOMETHING AND MODEL AFTER LISTRENTALSMANAGER AND MAPRENTALSMANAGER
            // TODO: THIS REALLY NEEDS ITS OWN FAVORITESRENTALSEVENT BECAUSE IT IS OVERWRITING MY MAIN LIST
            // TODO: MAKE SURE CAN UNSTAR RENTALS

            @Override
            public void onSuccess(String response) {
                ListRentals listRentals = GeekGson.getInstance().fromJson(response, ListRentals.class);

                if (listRentals != null && listRentals.all != null && listRentals.all.size() > 0) {
                    AppEventBus.post(new FavoriteRentalsEvent(listRentals.all));
                } else {
                    AppEventBus.post(new NoFavoritesEvent());
                }
            }
        });
    }

}
