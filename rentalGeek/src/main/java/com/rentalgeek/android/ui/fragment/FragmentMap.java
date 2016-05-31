package com.rentalgeek.android.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.bus.events.MapRentalsEvent;
import com.rentalgeek.android.bus.events.NoRentalsEvent;
import com.rentalgeek.android.bus.events.SetRentalEvent;
import com.rentalgeek.android.bus.events.ShowRentalEvent;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.mvp.map.MapPresenter;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.MapRental;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.ui.view.AutoCompleteAddressListener;
import com.rentalgeek.android.utils.OkAlert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMarkerClickListener, OnMapClickListener {

    private GoogleMap map;
    private MapPresenter presenter;
    private RentalView rentalView;
    private AutoCompleteTextView locationAutoCompleteTextView;
    private GoogleApiClient googleApiClient;

    /*
     * Need this for onClick of marker...since google made Marker class final and can not be extended....dumb
     * Idea is to use the auto generated marker Id as an association with the
     * rental id of a property. When clicking on a marker, reference this
     * hashmap to see which rental to show.
     */

    private HashMap<String, String> markerRentalMap = new HashMap<>();

    /*
     * Since Google doesnt let us iterate through markers...
     */
    private List<Marker> markers = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use

        rentalView = (RentalView) getChildFragmentManager().findFragmentById(R.id.rental);
        presenter = new MapPresenter();

        locationAutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.location_autocompletetextview);

        locationAutoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    dismissSearchEditText();
                    rentalView.hide();

                    String location = locationAutoCompleteTextView.getText().toString().trim();
                    if (!location.equals("")) {
                        ActivityHome activityHome = (ActivityHome) getActivity();
                        showProgressDialog(R.string.loading_rentals);
                        activityHome.presenter.getMapRentalOfferings(location);
                    }

                    return true;
                }

                return false;
            }
        });

        setUpGooglePlacesAutocomplete();

        return view;
    }

    private void setUpGooglePlacesAutocomplete() {
        googleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Places.GEO_DATA_API).build();
        PlaceAutocompleteAdapter locationSearchAdapter = new PlaceAutocompleteAdapter(getActivity(), googleApiClient);
        locationAutoCompleteTextView.setOnItemClickListener(new AutoCompleteAddressListener(locationAutoCompleteTextView));
        locationAutoCompleteTextView.setAdapter(locationSearchAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
        this.map.setOnMarkerClickListener(this);
        this.map.setOnMapClickListener(this);
    }

    @Override
    public void setRentals(ArrayList<MapRental> mapRentals) {
        if (map != null) {
            map.clear();
            markers.clear();
            markerRentalMap.clear();
            presenter.addRentals(mapRentals);
        }
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

        locationAutoCompleteTextView.clearFocus();
        dismissSearchEditText();

        return true;
    }

    @Override
    public void onMapClick(LatLng position) {
        rentalView.hide();
        dismissSearchEditText();
    }

    private void dismissSearchEditText() {
        locationAutoCompleteTextView.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationAutoCompleteTextView.getWindowToken(), 0);
    }

    public void onEventMainThread(MapRentalsEvent event) {
        if (event.getMapRentals() != null) {
            setRentals(event.getMapRentals());
        }
    }

    public void onEventMainThread(SetRentalEvent event) {
        if (event.getRental() != null) {
            Rental rental = event.getRental();
            zoomTo(rental.getLatitude(), rental.getLongitude(), 15);
            AppEventBus.post(new ShowRentalEvent(rental));
        }
    }

    public void onEventMainThread(NoRentalsEvent event) {
        hideProgressDialog();
        OkAlert.show(getActivity(), "No Results", "No properties were found for that location.");
    }

    public void onEventMainThread(AddMarkersEvent event) {
        if (event.getMarkers() != null) {
            if (map != null) {
                for (RentalMarker rentalMarker : event.getMarkers()) {
                    Marker mapMarker = map.addMarker(rentalMarker.getMarker());
                    markerRentalMap.put(mapMarker.getId(), Integer.toString(rentalMarker.getRental().id));
                    markers.add(mapMarker);
                }

                hideProgressDialog();
            }
        }
    }

}
