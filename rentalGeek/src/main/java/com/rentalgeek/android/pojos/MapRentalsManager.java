package com.rentalgeek.android.pojos;

import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.bus.events.MapRentalsEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

public class MapRentalsManager {

    private static final MapRentalsManager INSTANCE = new MapRentalsManager();

    private MapRentals mapRentals;

    private MapRentalsManager() {

    }

    public static MapRentalsManager getInstance() {
        return INSTANCE;
    }

    // TODO: ADD GET METHOD THAT ACCEPTS LAT,LNG,RADIUS (maybe let it accept requestparams argument instead of separate args)

    public void get() {
        if (mapRentals == null || mapRentals.all == null || mapRentals.all.size() == 0) {
            makeNetworkCall();
        } else {
            AppEventBus.post(new MapRentalsEvent(mapRentals.all));
        }
    }

    public void get(boolean forceRefresh) {
        if (forceRefresh) {
            makeNetworkCall();
        } else {
            get();
        }
    }

    private void makeNetworkCall() {
        GlobalFunctions.getApiCall("zzz", AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                mapRentals = GeekGson.getInstance().fromJson(content, MapRentals.class);
                AppEventBus.post(new MapRentalsEvent(mapRentals.all));
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                AppEventBus.post(new ErrorAlertEvent("Error", "There was an error loading the map."));
            }

            @Override
            public void onFinish() {
                GeekProgressDialog.dismiss();
            }
        });
    }
}
