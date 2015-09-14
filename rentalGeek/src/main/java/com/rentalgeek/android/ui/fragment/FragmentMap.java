package com.rentalgeek.android.ui.fragment;

import java.util.List;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import java.util.HashMap;
import java.util.LinkedList;
import android.view.ViewGroup;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import com.rentalgeek.android.pojos.Rental;
import com.google.android.gms.maps.GoogleMap;
import com.rentalgeek.android.mvp.map.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.RentalGeekApplication;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.rentalgeek.android.mvp.map.MapPresenter;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMarkerClickListener, OnMapClickListener {

	private static final String TAG = "FragmentMap";

    private GoogleMap map;
    private MapPresenter presenter;
    private RentalView rentalView;


    /*
     * Need this for onClick of marker...since google made Marker class final and can not be extended....dumb
     * Idea is to use the auto generated marker Id as an association with the
     * rental id of a property. When clicking on a marker, reference this
     * hashmap to see which rental to show.
     */

    private HashMap<String,String> markerRentalMap = new HashMap<String,String>();
    
    /*
     * Since Google doesnt let us iterate through markers...
     */
    private List<Marker> markers = new LinkedList<Marker>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.map, container, false);
        
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use
        
        rentalView = (RentalView)getChildFragmentManager().findFragmentById(R.id.rental);

        presenter = new MapPresenter(this);
        
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
            markers.clear();
            markerRentalMap.clear();
            presenter.addRentals(rentals);       
        }
    }

    @Override
    public void addMarker(RentalMarker rentalMarker) {
        Marker mapMarker = map.addMarker(rentalMarker.getMarker());
        markerRentalMap.put(mapMarker.getId(),rentalMarker.getRental().getId());
        markers.add(mapMarker);
    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),zoom));
    }

    @Override
    public void boundbox() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        
        for(Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        int width = RentalGeekApplication.getScreenWidth();
        
        int mapPadding = (int)RentalGeekApplication.getDimension(R.dimen.map_padding);
   
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),width,width,mapPadding));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String marker_id = marker.getId();
        String rental_id = markerRentalMap.get(marker_id);
        
        presenter.getRental(rental_id);

        return true;
    }

    @Override
    public void onMapClick(LatLng position) {
        rentalView.hide();
    }

    @Override
    public void setRental(Rental rental) {

         if( rental != null ) {
            zoomTo(rental.getLatitude(),rental.getLongitude(),15);
            rentalView.showRental(rental);    
        }
    }
}
