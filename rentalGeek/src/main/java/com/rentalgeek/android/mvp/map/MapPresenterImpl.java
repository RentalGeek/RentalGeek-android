package com.rentalgeek.android.mvp.map;

import rx.Observable;
import java.util.List;
import rx.Subscription;
import android.util.Log;
import rx.functions.Func1;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import com.rentalgeek.android.pojos.Rental;
import rx.android.schedulers.AndroidSchedulers;
import com.rentalgeek.android.utils.MarkerUtils;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.storage.RentalCache;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapPresenterImpl implements MapPresenter {
    
    private MapView mapView;

    public MapPresenterImpl(MapView mapView) {
        this.mapView = mapView;
    }
    
    @Override public void addRentals(Rental[] rentals) {
        if( rentals == null || rentals.length == 0 ) {
            return;
        }

        else {
            Observable<Rental> setRentalObservable = Observable.from(rentals);
            
            //Receives Rental objects and returns MarkerOptions
            setRentalObservable.map(new Func1<Rental,RentalMarker>() {
                @Override
                public RentalMarker call(Rental rental) {
                    RentalCache.getInstance().add(rental);
                    MarkerOptions marker = MarkerUtils.createRentalMarker(new LatLng(rental.getLatitude(),rental.getLongitude()),rental.getBedroomCount());

                    RentalMarker rentalMarker = new RentalMarker();
                    rentalMarker.setRental(rental);
                    rentalMarker.setMarker(marker);

                    return rentalMarker;
                }
            })
            .toList()
            .subscribeOn(Schedulers.newThread())//Want to do work on a new thread
            .observeOn(AndroidSchedulers.mainThread())//Want to receive results from work on main thread since we're going to tamper UI
            .subscribe( new Action1<List<RentalMarker>>() {
                @Override
                public void call(List<RentalMarker> markers) {

                    for( int i = 0; i < markers.size(); i++ ) {
                        
                        mapView.addMarker(markers.get(i)); 
                        
                        if( i == 0 ) { //Zoom to first marker
                            LatLng position =  markers.get(i).getMarker().getPosition();
                            mapView.zoomTo(position.latitude,position.longitude,15);
                        }
                    }
                }
            });
        }
    }
}
