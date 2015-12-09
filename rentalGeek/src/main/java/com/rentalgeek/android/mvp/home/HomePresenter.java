package com.rentalgeek.android.mvp.home;

import android.os.Bundle;
import android.util.Log;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.bus.events.NoRentalsEvent;
import com.rentalgeek.android.bus.events.SetRentalsEvent;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomePresenter implements Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();
    Map<String, Integer> numDuplicatesAtLocation = new HashMap<>();

    @Override
    public void getRentalOfferings(String location) {
        String url = ApiManager.getPropertySearchUrl();
        if (!location.equals("")) {
            url += "?search[location]=" + location;
        }

        String token = AppPreferences.getAuthToken();

        GlobalFunctions.getApiCall(null, url, token, new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("onStart called.");
            }

            @Override
            public void onFinish() {
                System.out.println("onFinish called.");
            }

            @Override
            public void onSuccess(String response) {
                try {
                    System.out.println(response);

                    JSONObject json = new JSONObject(response);
                    JSONArray rentalOfferings = json.getJSONArray("rental_offerings");

                    Rental[] rentals = GeekGson.getInstance().fromJson(rentalOfferings.toString(), Rental[].class);
                    Rental[] offsetRentals = offsetDuplicateLocations(rentals);

                    if (offsetRentals.length > 0) {
                        System.out.println(String.format("Found %d rentals based on query.", offsetRentals.length));

                        for (Rental rental : offsetRentals) {
                            RentalCache.getInstance().add(rental);
                        }

                        AppEventBus.post(new SetRentalsEvent(offsetRentals));
                    } else {
                        AppEventBus.post(new NoRentalsEvent());
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onAuthenticationFailed() {
                System.out.println("Authentication failed.");
            }

            @Override
            public void onFailure(Throwable throwable, String response) {
                System.out.println(String.format("Error: %s", response));
                String title = RentalGeekApplication.getResourceString(R.string.home);
                String message = RentalGeekApplication.getResourceString(R.string.oops);
                AppEventBus.post(new ErrorAlertEvent(title, message));
            }
        });
    }

    private Rental[] offsetDuplicateLocations(Rental[] originalRentals) {
        Set<String> locations = new HashSet<>();
        List<Rental> offsetRentals = new ArrayList<>();

        for (Rental rental : originalRentals) {
            if (!locations.add(rental.coordinates())) {
                Rental offsetRental = slightlyOffsetDuplicate(rental);
                offsetRentals.add(offsetRental);
            } else {
                offsetRentals.add(rental);
            }
        }

        return offsetRentals.toArray(new Rental[offsetRentals.size()]);
    }

    private Rental slightlyOffsetDuplicate(Rental rental) {
        Integer countAtThisLocation = 1;
        if (numDuplicatesAtLocation.containsKey(rental.coordinates())) {
            countAtThisLocation = numDuplicatesAtLocation.get(rental.coordinates());
            numDuplicatesAtLocation.put(rental.coordinates(), ++countAtThisLocation);
        } else {
            numDuplicatesAtLocation.put(rental.coordinates(), countAtThisLocation);
        }

        Rental offsetRental = rental;
        String tinyOffset = "0.00" + Integer.toString(countAtThisLocation);
        Double tinyOffsetNum = Double.parseDouble(tinyOffset);
        offsetRental.offsetLatitude(tinyOffsetNum);
        offsetRental.offsetLongitude(tinyOffsetNum);

        return offsetRental;
    }

    @Override
    public void getRentalOfferings(Bundle bundle) {
        Rental[] rentals = {};

        if (bundle != null) {
            ArrayList<String> rental_ids = bundle.getStringArrayList("RENTALS");
            int size = rental_ids.size();
            rentals = new Rental[size];

            for (int i = 0; i < size; i++) {
                rentals[i] = RentalCache.getInstance().get(rental_ids.get(i));
            }

            AppEventBus.post(new SetRentalsEvent(rentals));
        } else {
            String title = RentalGeekApplication.getResourceString(R.string.home);
            String message = RentalGeekApplication.getResourceString(R.string.oops);
            AppEventBus.post(new ErrorAlertEvent(title, message));
        }
    }
}
