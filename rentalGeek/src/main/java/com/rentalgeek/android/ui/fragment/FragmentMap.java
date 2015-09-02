package com.rentalgeek.android.ui.fragment;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.map.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.rentalgeek.android.ui.map.MapPresenterImpl;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView{

	private static final String TAG = "FragmentMap";

    private GoogleMap map;
    private MapPresenterImpl presenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.map, container, false);
        
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use

        presenter = new MapPresenterImpl(this);

		return view;
	}

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
    }

    public void setRentals(Rental[] rentals) {
        if( map != null ) {
            map.clear();
            presenter.addRentals(rentals);       
        }
    }

    @Override
    public void addMarker(MarkerOptions marker) {
        map.addMarker(marker);
    }
}
