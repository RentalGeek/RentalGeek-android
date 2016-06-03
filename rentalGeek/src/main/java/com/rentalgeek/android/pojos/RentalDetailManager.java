package com.rentalgeek.android.pojos;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.RentalDetailErrorEvent;
import com.rentalgeek.android.bus.events.RentalDetailEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;
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

    public void get(String id) {
        RentalDetail cachedRental = rentalDetails.get(id);
        if (cachedRental != null) {
            AppEventBus.post(new RentalDetailEvent(cachedRental));
        } else {
            getFromNetwork(id);
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

            // TODO: HANDLE THE FAILURE EVENT IN CALLING ACTIVITY OR FRAGMENT

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                AppEventBus.post(new RentalDetailErrorEvent());
            }

            @Override
            public void onFinish() {
                GeekProgressDialog.dismiss();
            }
        });
    }
}
