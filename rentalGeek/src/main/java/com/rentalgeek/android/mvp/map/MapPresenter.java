package com.rentalgeek.android.mvp.map;

import android.content.Context;

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

    // TODO: (NOW) LOADING STATE FOR GETTING DETAIL INFO FOR BOTH MAP AND LIST TAPS
    // TODO: (MINOR) CAN I REFACTOR TO PASS AN INT FOR ID INSTEAD OF A STRING

    @Override
    public void getRental(String rental_id) {
        RentalDetailManager.getInstance().get(rental_id);
    }

    @Override
    public void addRentals(final Context context, ArrayList<MapRental> mapRentals) {
        if (mapRentals != null && mapRentals.size() != 0) {
            Observable<MapRental> setRentalObservable = Observable.from(mapRentals);

            setRentalObservable.filter(new Func1<MapRental, Boolean>() {
                @Override
                public Boolean call(MapRental mapRental) {
                    return mapRental.latitude != null;
                }
            }).map(new Func1<MapRental, RentalMarker>() {
                @Override
                public RentalMarker call(MapRental mapRental) {
                    MarkerOptions marker = MarkerUtils.createRentalMarker(context, new LatLng(mapRental.latitude, mapRental.longitude), mapRental.bedroomCount);

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
