package com.rentalgeek.android.pojos;

import android.support.annotation.Nullable;

import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.Map;

public abstract class RentalsManager {

    protected abstract void rentalsReceived(String response);
    protected abstract void sendExistingRentals();
    protected abstract String baseUrl();
    protected abstract boolean hasRentalsCached();

    public void getAll(@Nullable Map<String, String> requestParams) {
        if (hasRentalsCached()) {
            sendExistingRentals();
            GeekProgressDialog.dismiss();
        } else {
            makeNetworkCall(requestParams);
        }
    }

    public void getAll(@Nullable Map<String, String> requestParams, boolean forceRefresh) {
        if (forceRefresh) {
            makeNetworkCall(requestParams);
        } else {
            getAll(requestParams);
        }
    }

    private void makeNetworkCall(@Nullable Map<String, String> requestParams) {
        String appendedUrlParams = "";

        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                appendedUrlParams += "&" + entry.getKey() + "=" + entry.getValue();
            }
        }

        makeNetworkCall(baseUrl() + appendedUrlParams);
    }

    private void makeNetworkCall(String url) {
        GlobalFunctions.getApiCall(url, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                rentalsReceived(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                AppEventBus.post(new ErrorAlertEvent("Error", "There was an error loading the rentals."));
            }

            @Override
            public void onFinish() {
                GeekProgressDialog.dismiss();
            }
        });
    }

}
