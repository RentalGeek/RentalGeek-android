package com.rentalgeek.android.mvp.home;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.constants.JsonKey;
import com.rentalgeek.android.constants.ManhattanKansas;
import com.rentalgeek.android.pojos.MapRentalsManager;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.dialog.GeekProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class HomePresenter implements Presenter {

    private ManhattanKansas manhattanKansas;

    // TODO: ONLY MAKE MAP API CALL WHEN ON MAP TAB FIRST LOAD, DON'T MAKE LIST API CALL UNTIL SWAPPING TABS TO LIST

    public HomePresenter(ManhattanKansas manhattanKansas) {
        this.manhattanKansas = manhattanKansas;
    }

    @Override
    public void getRentalOfferings(String location) {

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(JsonKey.LATITUDE, manhattanKansas.latitude());
        requestParams.put(JsonKey.LONGITUDE, manhattanKansas.longitude());
        requestParams.put(JsonKey.RADIUS, manhattanKansas.radius());
        MapRentalsManager.getInstance().get(requestParams);






//        String url = ApiManager.loadMapPinData();
////        if (!location.equals("")) {
////            url += "?search[location]=" + location;
////        }
//
//        String token = AppPreferences.getAuthToken();
//
//        System.out.println(url);
//        System.out.println(token);
//
//        GlobalFunctions.getApiCall(url, token, new GeekHttpResponseHandler() {
//            @Override
//            public void onStart() {
//                System.out.println("onStart called.");
//            }
//
//            @Override
//            public void onFinish() {
//                System.out.println("onFinish called.");
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    GeekProgressDialog.dismiss();
//
//                    MapRentals mapRentals = GeekGson.getInstance().fromJson(response, MapRentals.class);
//
//                    if (mapRentals.all != null && mapRentals.all.size() > 0) {
//                        MapRentalCache.INSTANCE.mapRentals = mapRentals.all;
//                        AppEventBus.post(new SetRentalsEvent(rentalOfferings.all));
//                    } else {
//                        AppEventBus.post(new NoRentalsEvent());
//                    }
//
////                    JSONObject json = new JSONObject(response);
////                    JSONArray rentalOfferings = json.getJSONArray("rental_offerings");
////
////                    Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(), Rental[].class);
////
////                    if (rentals != null && rentals.length > 0) {
////
////                        System.out.println(String.format("Found %d rentals based on query.", rentals.length));
////
////                        for (Rental rental : rentals) {
////                            RentalCache.getInstance().add(rental);
////                        }
////
////                        AppEventBus.post(new SetRentalsEvent(rentals));
////                    } else {
////                        AppEventBus.post(new NoRentalsEvent());
////                    }
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                }
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                System.out.println("Authentication failed.");
//                GeekProgressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Throwable throwable, String response) {
//                System.out.println(String.format("Error: %s", response));
//                String title = RentalGeekApplication.getResourceString(R.string.home);
//                String message = RentalGeekApplication.getResourceString(R.string.oops);
//                GeekProgressDialog.dismiss();
//                AppEventBus.post(new ErrorAlertEvent(title, message));
//            }
//        });
    }

    @Override
    public void getRentalOfferings(Bundle bundle) {
        Rental[] rentals = {};

        if (bundle != null) {
//            ArrayList<String> rental_ids = bundle.getStringArrayList("RENTALS");
//
//            int size = rental_ids.size();
//
//            rentals = new Rental[size];
//
//            for (int i = 0; i < size; i++) {
//                rentals[i] = RentalCache.getInstance().get(rental_ids.get(i));
//            }

//            AppEventBus.post(new SetRentalsEvent(CompressedRentalCache.INSTANCE.rentalOfferings));
//            AppEventBus.post(new SetRentalsEvent(rentals));
        } else {
            String title = RentalGeekApplication.getResourceString(R.string.home);
            String message = RentalGeekApplication.getResourceString(R.string.oops);
            GeekProgressDialog.dismiss();
            AppEventBus.post(new ErrorAlertEvent(title, message));
        }
    }

}
