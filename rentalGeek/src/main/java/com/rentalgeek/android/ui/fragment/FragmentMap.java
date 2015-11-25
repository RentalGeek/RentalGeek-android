package com.rentalgeek.android.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.bus.events.SetRentalEvent;
import com.rentalgeek.android.bus.events.SetRentalsEvent;
import com.rentalgeek.android.bus.events.ShowRentalEvent;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.mvp.map.MapPresenter;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.Rental;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMarkerClickListener, OnMapClickListener {

    private static final String TAG = "FragmentMap";

    private GoogleMap map;
    private MapPresenter presenter;
    private RentalView rentalView;
    private EditText locationEditText;

    /*
     * Need this for onClick of marker...since google made Marker class final and can not be extended....dumb
     * Idea is to use the auto generated marker Id as an association with the
     * rental id of a property. When clicking on a marker, reference this
     * hashmap to see which rental to show.
     */

    private HashMap<String, String> markerRentalMap = new HashMap<String, String>();

    /*
     * Since Google doesnt let us iterate through markers...
     */
    private List<Marker> markers = new LinkedList<Marker>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use

        rentalView = (RentalView) getChildFragmentManager().findFragmentById(R.id.rental);
        presenter = new MapPresenter();

        locationEditText = (EditText)view.findViewById(R.id.location_edittext);

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
        if (map != null) {
            map.clear();
            markers.clear();
            markerRentalMap.clear();
            presenter.addRentals(rentals);
        }
    }

    @Override
    public void boundbox() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        int width = RentalGeekApplication.getScreenWidth();
        int mapPadding = (int) RentalGeekApplication.getDimension(R.dimen.map_padding);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, width, mapPadding));
    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String marker_id = marker.getId();
        String rental_id = markerRentalMap.get(marker_id);

        presenter.getRental(rental_id);

        locationEditText.clearFocus();
        dismissSearchEditText();

        return true;
    }

    @Override
    public void onMapClick(LatLng position) {
        rentalView.hide();
        dismissSearchEditText();
    }

    private void dismissSearchEditText() {
        locationEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
    }

    public void onEventMainThread(SetRentalsEvent event) {
        if (event.getRentals() != null) {
            setRentals(event.getRentals());
        }
    }

    public void onEventMainThread(SetRentalEvent event) {
        if (event.getRental() != null) {
            Rental rental = event.getRental();
            zoomTo(rental.getLatitude(), rental.getLongitude(), 15);
            AppEventBus.post(new ShowRentalEvent(rental));
        }
    }

    public void onEventMainThread(AddMarkersEvent event) {
        if (event.getMarkers() != null) {
            if (map != null) {
                for (RentalMarker rentalMarker : event.getMarkers()) {
                    Marker mapMarker = map.addMarker(rentalMarker.getMarker());
                    markerRentalMap.put(mapMarker.getId(), rentalMarker.getRental().getId());
                    markers.add(mapMarker);
                }

                boundbox();
                hideProgressDialog();
            }
        }
    }
}
