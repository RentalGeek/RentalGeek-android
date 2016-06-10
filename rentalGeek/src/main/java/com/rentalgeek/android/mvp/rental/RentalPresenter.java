package com.rentalgeek.android.mvp.rental;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppliedEvent;
import com.rentalgeek.android.bus.events.ShowCosignApplicationEvent;
import com.rentalgeek.android.bus.events.ShowNeedPaymentEvent;
import com.rentalgeek.android.bus.events.ShowProfileCreationEvent;
import com.rentalgeek.android.bus.events.ShowPropertyPhotosEvent;
import com.rentalgeek.android.mvp.common.StarPresenter;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.PropertyPhotosRootDTO;
import com.rentalgeek.android.pojos.RentalDetailManager;
import com.rentalgeek.android.ui.preference.AppPreferences;

import org.json.JSONObject;

public class RentalPresenter extends StarPresenter implements Presenter {

    private static final String TAG = RentalPresenter.class.getSimpleName();

    @Override
    public void apply(String rental_id) {
        if (rental_id == null || rental_id.isEmpty()) {
            return;
        }
        if (SessionManager.Instance.hasPayed()) {
            sendApplication(rental_id);
            return;
        }
        if (SessionManager.Instance.hasProfile()) {
            AppEventBus.post(new ShowNeedPaymentEvent());
            return;
        }
        if (SessionManager.Instance.getCurrentUser().is_cosigner) {
            AppEventBus.post(new ShowCosignApplicationEvent());
            return;
        }

        AppEventBus.post(new ShowProfileCreationEvent());
    }

    private void sendApplication(String rental_id) {
        String url = ApiManager.postApplication();
        String token = AppPreferences.getAuthToken();

        RequestParams params = new RequestParams();
        params.put("application[rental_offering_id]", rental_id);

        if (SessionManager.Instance.getCurrentUser().is_cosigner) {
            params.put("application[as_cosigner]", "true");
        }

        GlobalFunctions.postApiCall(null, url, params, token, new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    System.out.println(response);
                    JSONObject json = new JSONObject(response);

                    if (json.has("application")) {
                        AppEventBus.post(new AppliedEvent());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable ex, String response) {
                try {
                    System.out.println(response);
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        boolean success = json.getBoolean("success");

                        if (!success) {
                            if (json.has("is_cosigner")) {
                                boolean is_cosigner = json.getBoolean("is_cosigner");

                                if (is_cosigner) {
                                    AppEventBus.post(new ShowCosignApplicationEvent());
                                } else {
                                    AppEventBus.post(new ShowProfileCreationEvent());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void getRental(String rental_id) {
        RentalDetailManager.getInstance().get(rental_id);
    }

    @Override
    public void getPropertyPhotos(String rental_id) {
        GlobalFunctions.getApiCall(ApiManager.propertyPhotos(rental_id), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                PropertyPhotosRootDTO propertyPhotosRootDTO = new Gson().fromJson(content, PropertyPhotosRootDTO.class);
                AppEventBus.post(new ShowPropertyPhotosEvent(propertyPhotosRootDTO.property_photos));
            }
        });
    }

}
