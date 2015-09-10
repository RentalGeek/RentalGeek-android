package com.rentalgeek.android.mvp.map;

import rx.Observable;
import java.util.List;
import rx.Subscription;
import android.util.Log;
import rx.functions.Func1;
import org.json.JSONArray;
import org.json.JSONObject;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.api.ApiManager;
import rx.android.schedulers.AndroidSchedulers;
import com.rentalgeek.android.utils.MarkerUtils;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.net.GlobalFunctions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.ui.preference.AppPreferences;

public class MapPresenterImpl implements MapPresenter {
 
    private static final String TAG = MapPresenterImpl.class.getSimpleName();

    private MapView mapView;
    
    public MapPresenterImpl(MapView mapView) {
        this.mapView = mapView;
    }
    
    @Override public void getRental(String rental_id) {
        Rental rental = RentalCache.getInstance().get(rental_id);
        
        if( rental == null ) {

            System.out.println("Not found in cache");
            String url = ApiManager.getRental(rental_id);
            String token = AppPreferences.getAuthToken();
            
            GlobalFunctions.getApiCall(null,url,token, new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {

                    try {
                        Log.i(TAG,response);
                        JSONObject json = new JSONObject(response);

                        if( json.has("rental_offering") ) {
                            JSONObject rental_json = json.getJSONObject("rental_offering");
                            Rental rental = GeekGson.getInstance().fromJson(rental_json.toString(),Rental.class);
                            RentalCache.getInstance().add(rental);
                            mapView.setRental(rental);
                        }
                    }

                    catch(Exception e) {
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }
        
        else
            mapView.setRental(rental);
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
                    }

                    mapView.boundbox();
                }
            });
        }
    }
}
