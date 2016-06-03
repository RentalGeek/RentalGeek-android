package com.rentalgeek.android.mvp.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.pojos.MapRental;
import com.rentalgeek.android.pojos.RentalDetailManager;
import com.rentalgeek.android.utils.MarkerUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MapPresenter implements Presenter {

    private static final String TAG = MapPresenter.class.getSimpleName();

    // TODO: STOP USING RENTALCACHE HERE AND MAKE MY OWN RENTAL MODEL OBJ THAT INCLUDES EVERYTHING AND MAKE A MANAGER FOR IT
    // TODO: TAKE OUT ALL USES OF RENTAL AND MAKE THEM RENTALDETAIL, THEN DELETE RENTAL AND RENAME RENTALDETAIL TO RENTAL
    // TODO: SUCCESS IS SENDING A SETRENTALEVENT BUT I SHOULD REFACTOR TO USE MY RENTALDETAILEVENT
    // TODO: DO THIS REFACTOR FOR GETTING DETAIL FROM MAP PIN TAP AND ALSO LIST ROW TAP
    // TODO: LOADING STATE FOR GETTING DETAIL INFO FOR BOTH MAP AND LIST TAPS

    // TODO: (MINOR) CAN I REFACTOR TO PASS AN INT FOR ID INSTEAD OF A STRING

    @Override
    public void getRental(String rental_id) {


        RentalDetailManager.getInstance().get(rental_id);


//        Rental rental = RentalCache.getInstance().get(rental_id);
//
//        if (rental == null) {
//            System.out.println("Not found in cache");
//            String url = ApiManager.getRental(rental_id);
//            String token = AppPreferences.getAuthToken();
//
//            GlobalFunctions.getApiCall(url, token, new GeekHttpResponseHandler() {
//                @Override
//                public void onSuccess(String response) {
//                    try {
//                        Log.i(TAG, response);
//                        JSONObject json = new JSONObject(response);
//
//                        if (json.has("rental_offering")) {
//                            JSONObject rental_json = json.getJSONObject("rental_offering");
//                            Rental rental = GeekGson.getInstance().fromJson(rental_json.toString(), Rental.class);
//                            RentalCache.getInstance().add(rental);
//
//
//                            AppEventBus.post(new SetRentalEvent(rental));
//                        }
//                    } catch (Exception e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//                }
//            });
//        } else {
//            AppEventBus.post(new SetRentalEvent(rental));
//        }
    }

    @Override
    public void addRentals(ArrayList<MapRental> mapRentals) {
        if (mapRentals == null || mapRentals.size() == 0) {
            return;
        } else {
            Observable<MapRental> setRentalObservable = Observable.from(mapRentals);

            setRentalObservable.filter(new Func1<MapRental, Boolean>() {
                @Override
                public Boolean call(MapRental mapRental) {
                    return mapRental.latitude != null;
                }
            }).map(new Func1<MapRental, RentalMarker>() {
                @Override
                public RentalMarker call(MapRental mapRental) {
                    MarkerOptions marker = MarkerUtils.createRentalMarker(new LatLng(mapRental.latitude, mapRental.longitude), mapRental.bedroomCount);

                    RentalMarker rentalMarker = new RentalMarker();
                    rentalMarker.setRental(mapRental);
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
