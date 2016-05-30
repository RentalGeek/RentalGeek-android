package com.rentalgeek.android.pojos;

import android.support.annotation.Nullable;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.bus.events.MapRentalsEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

import java.util.Map;

public class MapRentalsManager {

    private static final MapRentalsManager INSTANCE = new MapRentalsManager();

    private MapRentals mapRentals;

    private MapRentalsManager() {

    }

    public static MapRentalsManager getInstance() {
        return INSTANCE;
    }

    public void get(@Nullable Map<String, String> requestParams) {
        if (mapRentals == null || mapRentals.all == null || mapRentals.all.size() == 0) {
            makeNetworkCall(requestParams);
        } else {
            AppEventBus.post(new MapRentalsEvent(mapRentals.all));
        }
    }

    public void get(@Nullable Map<String, String> requestParams, boolean forceRefresh) {
        if (forceRefresh) {
            makeNetworkCall(requestParams);
        } else {
            get(requestParams);
        }
    }

    private void makeNetworkCall(@Nullable Map<String, String> requestParams) {

        String appendedUrlParams = "";

        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                appendedUrlParams += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }

        GlobalFunctions.getApiCall(ApiManager.loadMapPinData() + appendedUrlParams, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
