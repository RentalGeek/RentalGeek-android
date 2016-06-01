package com.rentalgeek.android.mvp.common;

import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.SelectStarEvent;
import com.rentalgeek.android.bus.events.UnSelectStarEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.ListRental;
import com.rentalgeek.android.pojos.ListRentalsManager;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.ui.preference.AppPreferences;

import org.json.JSONObject;

public abstract class StarPresenter {

    private static final String TAG = StarPresenter.class.getSimpleName();

    public void select(final String rental_id, int position) {
        if (rental_id == null || rental_id.isEmpty() || position < 0)
            return;
        else {
            ListRental selectedRental = ListRentalsManager.getInstance().get(rental_id);
            if (selectedRental != null && selectedRental.starredPropertyId != null) {
                unselectStar(rental_id, position);
            } else {
                String user_id = SessionManager.Instance.getCurrentUser().id;
                selectStar(rental_id, user_id, position);
            }
        }
    }

    public void select(final String rental_id) {
        if (rental_id == null || rental_id.isEmpty())
            return;
        else {
            boolean starred = RentalCache.getInstance().get(rental_id).isStarred();

            if (!starred) {
                String user_id = SessionManager.Instance.getCurrentUser().id;
                selectStar(rental_id, user_id, 0);
            } else {
                unselectStar(rental_id, 0);
            }
        }
    }

    private void selectStar(final String rental_id, final String user_id, final int position) {

        if ((rental_id == null || rental_id.isEmpty()) || (user_id == null || user_id.isEmpty()) || position < 0)
            return;

        RequestParams params = new RequestParams();
        params.put("starred_property[user_id]", user_id);
        params.put("starred_property[rental_offering_id]", rental_id);

        String url = ApiManager.postRentalStar();
        String token = AppPreferences.getAuthToken();

        System.out.println(url);

        GlobalFunctions.postApiCall(null, url, params, token, new GeekHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject resp_json = new JSONObject(response);
                    if (resp_json.has("starred_property")) {
                        JSONObject rental_json = resp_json.getJSONObject("starred_property");
                        if (rental_json.has("id")) {
                            ListRental rental = ListRentalsManager.getInstance().get(rental_id);
                            rental.starredPropertyId = Integer.parseInt(rental_json.getString("id"));
                            AppEventBus.post(new SelectStarEvent(position));
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

        });
    }

    private void unselectStar(final String rental_id, final int position) {
        if (rental_id == null || rental_id.isEmpty() || position < 0)
            return;
        else {
            String star_id = Integer.toString(ListRentalsManager.getInstance().get(rental_id).starredPropertyId);
            String url = ApiManager.deleteRentalStar(star_id);
            String token = AppPreferences.getAuthToken();
            GlobalFunctions.deleteApiCall(null, url, token, new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    ListRental rental = ListRentalsManager.getInstance().get(rental_id);
                    rental.starredPropertyId = null;
                    AppEventBus.post(new UnSelectStarEvent(position));
                }
            });
        }
    }

}
