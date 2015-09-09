package com.rentalgeek.android.ui.fragment;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import java.util.HashMap;
import android.view.ViewGroup;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import com.rentalgeek.android.pojos.Rental;
import com.google.android.gms.maps.GoogleMap;
import com.rentalgeek.android.mvp.map.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.storage.RentalCache;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.rentalgeek.android.mvp.map.MapPresenterImpl;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMarkerClickListener, OnMapClickListener {

	private static final String TAG = "FragmentMap";

    private GoogleMap map;
    private MapPresenterImpl presenter;
    private RentalView rentalView;

    /*
     * Need this for onClick of marker...since google made Marker class final and can not be extended....dumb
     * Idea is to use the auto generated marker Id as an association with the
     * rental id of a property. When clicking on a marker, reference this
     * hashmap to see which rental to show.
     */

    private HashMap<String,String> markerRentalMap = new HashMap<String,String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.map, container, false);
        
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use
        
        rentalView = (RentalView)getChildFragmentManager().findFragmentById(R.id.rental);

        presenter = new MapPresenterImpl(this);
        
		return view;
	}

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnMapClickListener(this);
    }
    
    @Override
    public void setRentals(Rental[] rentals) {
        if( map != null ) {
            map.clear();
            markerRentalMap.clear();
            presenter.addRentals(rentals);       
        }
    }

    @Override
    public void addMarker(RentalMarker rentalMarker) {
        Marker mapMarker = map.addMarker(rentalMarker.getMarker());
        markerRentalMap.put(mapMarker.getId(),rentalMarker.getRental().getId());
    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),zoom));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerId = marker.getId();
        String rentalId = markerRentalMap.get(markerId);

        Rental rental = RentalCache.getInstance().get(rentalId);
        
        if( rental != null ) {
            rentalView.showRental(rental);    
        }
    
        return true;
    }

    @Override
    public void onMapClick(LatLng position) {
        rentalView.hide();
    }
}
