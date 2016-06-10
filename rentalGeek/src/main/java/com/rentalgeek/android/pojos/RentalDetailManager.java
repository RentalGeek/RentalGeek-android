package com.rentalgeek.android.pojos;

import android.util.Log;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.RentalDetailErrorEvent;
import com.rentalgeek.android.bus.events.RentalDetailEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.HashMap;
import java.util.Map;

public class RentalDetailManager {

    private static final RentalDetailManager INSTANCE = new RentalDetailManager();

    private Map<String, RentalDetail> rentalDetails = new HashMap<>();

    private RentalDetailManager() {

    }

    public static RentalDetailManager getInstance() {
        return INSTANCE;
    }

    public RentalDetail getFromCache(String id) {
        if (id != null) {
            RentalDetail cachedRental = rentalDetails.get(id);
            return cachedRental;
        }

        return null;
    }

    public void get(String id) {
        if (id != null) {
            if (getFromCache(id) != null) {
                AppEventBus.post(new RentalDetailEvent(getFromCache(id)));
            } else {
                getFromNetwork(id);
            }
        }
    }

    private void getFromNetwork(String id) {
        GlobalFunctions.getApiCall(ApiManager.getRental(id), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                RentalDetail rentalDetail = RentalDetail.fromJson(content);
                AppEventBus.post(new RentalDetailEvent(rentalDetail));
                rentalDetails.put(Integer.toString(rentalDetail.id), rentalDetail);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                AppEventBus.post(new RentalDetailErrorEvent());
            }
        });
    }
}
