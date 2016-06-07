package com.rentalgeek.android.pojos;

import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.FilterParams;

public abstract class RentalsManager {

    protected abstract void rentalsReceived(String response);
    protected abstract void sendExistingRentals();
    protected abstract String baseUrl();
    protected abstract boolean hasRentalsCached();
    private String lastFilterParams = "";

    public void getAll() {
        if (hasRentalsCached() && lastFilterParams.equals(FilterParams.INSTANCE.toString())) {
            sendExistingRentals();
            GeekProgressDialog.dismiss();
        } else {
            lastFilterParams = FilterParams.INSTANCE.toString();
            makeNetworkCall();
        }
    }

    private void makeNetworkCall() {
        makeNetworkCall(baseUrl() + FilterParams.INSTANCE.toString());
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
        });
    }

}
