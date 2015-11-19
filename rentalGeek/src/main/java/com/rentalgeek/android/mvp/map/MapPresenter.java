package com.rentalgeek.android.mvp.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.bus.events.SetRentalEvent;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.utils.MarkerUtils;

import org.json.JSONObject;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MapPresenter implements Presenter {

    private static final String TAG = MapPresenter.class.getSimpleName();

    @Override
    public void getRental(String rental_id) {
        Rental rental = RentalCache.getInstance().get(rental_id);

        if (rental == null) {
            System.out.println("Not found in cache");
            String url = ApiManager.getRental(rental_id);
            String token = AppPreferences.getAuthToken();

            GlobalFunctions.getApiCall(null, url, token, new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        Log.i(TAG, response);
                        JSONObject json = new JSONObject(response);

                        if (json.has("rental_offering")) {
                            JSONObject rental_json = json.getJSONObject("rental_offering");
                            Rental rental = GeekGson.getInstance().fromJson(rental_json.toString(), Rental.class);
                            RentalCache.getInstance().add(rental);
                            AppEventBus.post(new SetRentalEvent(rental));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } else {
            AppEventBus.post(new SetRentalEvent(rental));
        }
    }

    @Override
    public void addRentals(Rental[] rentals) {
        if (rentals == null || rentals.length == 0) {
            return;
        } else {
            Observable<Rental> setRentalObservable = Observable.from(rentals);

            //Receives Rental objects and returns MarkerOptions
            setRentalObservable.map(new Func1<Rental, RentalMarker>() {
                @Override
                public RentalMarker call(Rental rental) {
                    RentalCache.getInstance().add(rental);
                    MarkerOptions marker = MarkerUtils.createRentalMarker(new LatLng(rental.getLatitude(), rental.getLongitude()), rental.getBedroomCount());

                    if (rental.getLatitude() == 39.0141289) {
                        Log.d("tagzzz", "{ \"lat\" : " + rental.getLatitude() + ", \"lng\" : " + rental.getLongitude() + " },");
                        Log.d("tagzzz", "bed count: " + rental.getBedroomCount());
                    }

                    RentalMarker rentalMarker = new RentalMarker();
                    rentalMarker.setRental(rental);
                    rentalMarker.setMarker(marker);

                    return rentalMarker;
                }
            })
                    .toList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<RentalMarker>>() {
                        @Override
                        public void call(List<RentalMarker> markers) {
                            AppEventBus.post(new AddMarkersEvent(markers));
                        }
                    });
        }
    }
}
