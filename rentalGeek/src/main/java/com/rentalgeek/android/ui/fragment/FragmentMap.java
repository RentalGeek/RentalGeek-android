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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.bus.events.MapChangedEvent;
import com.rentalgeek.android.bus.events.MapRentalsEvent;
import com.rentalgeek.android.bus.events.NoRentalsEvent;
import com.rentalgeek.android.bus.events.RentalDetailEvent;
import com.rentalgeek.android.bus.events.ShowRentalEvent;
import com.rentalgeek.android.model.FetchArea;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.mvp.map.MapPresenter;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.MapRental;
import com.rentalgeek.android.pojos.RentalDetail;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.ui.view.AutoCompleteAddressListener;
import com.rentalgeek.android.utils.FilterParams;
import com.rentalgeek.android.utils.OkAlert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMarkerClickListener, OnMapClickListener, GoogleMap.OnCameraChangeListener {

    private GoogleMap map;
    private MapPresenter presenter;
    private RentalView rentalView;
    private AutoCompleteTextView locationAutoCompleteTextView;
    private GoogleApiClient googleApiClient;
    private HashMap<Integer, MapRental> alreadyShownPins = new HashMap<>();

    /*
     * Need this for onClick of marker...since google made Marker class final and can not be extended....dumb
     * Idea is to use the auto generated marker Id as an association with the
     * rental id of a property. When clicking on a marker, reference this
     * hashmap to see which rental to show.
     */

    private HashMap<Marker, String> markerRentalMap = new HashMap<>();

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

                        // TODO: CONVERT LOCATION TO COORDS THEN PUT THOSE COORDS IN FILTERPARAMS THEN CALL GETMAPRENTALOFFERINGS

//                        activityHome.presenter.getMapRentalOfferings(location);
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
        this.map.getUiSettings().setRotateGesturesEnabled(false);
        this.map.getUiSettings().setTiltGesturesEnabled(false);
        this.map.setOnCameraChangeListener(this);

        // TODO: CONVERT ALL PARAMS KEYS TO HAVE CONSTANTS CLASS
        // TODO: (NOW) SAVE FETCHAREA INFO TO APPPREFS AND RE-USE LAST ONE ON NEXT APP LAUNCH
        // TODO: (NOW) SHOW A NON OBSTRUCTING LOADING INDICATOR WHEN FETCHING NEW PINS OR LIST
        // TODO: (NOW) WHAT IS UP WITH THESE WHITE SQUARES SHOWING FOR PINS AFTER A FILTER SOMETIMES (GOTTA BE RELATED TO MY REMOVAL OF OLD MARKERS SOMEHOW)
            // appears to be an issue with google play services, might need to update
        // TODO: WHEN CLICKING A PIN, THEN GOING TO FILTER, THEN WHEN COMES BACK TO MAP THE PIN SHEET SHOWS AT BOTTOM AGAIN
    }

    @Override
    public void setRentals(ArrayList<MapRental> mapRentals) {
        if (map != null) {
            ArrayList<MapRental> rentalsToAdd = new ArrayList<>();
            for (MapRental rental : mapRentals) {
                if (alreadyShownPins.get(rental.id) == null) {
                    rentalsToAdd.add(rental);
                    alreadyShownPins.put(rental.id, rental);
                }
            }

            // loop through all alreadyShownPins and if one found that is not in mapRentals then remove it from map
            ArrayList<String> rentalsToRemove = new ArrayList<>();

            Iterator it = alreadyShownPins.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if (!mapRentals.contains((MapRental)pair.getValue())) {
                    rentalsToRemove.add((Integer.toString(((MapRental)pair.getValue()).id)));
                    it.remove();
                }
            }

            presenter.addRentals(rentalsToAdd);
            removeMarkers(rentalsToRemove);
        }


    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String rental_id = markerRentalMap.get(marker);
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

    public void onEventMainThread(RentalDetailEvent event) {
        if (event.getRentalDetail() != null) {
            RentalDetail rentalDetail = event.getRentalDetail();
            AppEventBus.post(new ShowRentalEvent(rentalDetail));
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
                    markerRentalMap.put(mapMarker, Integer.toString(rentalMarker.getRental().id));
                    markers.add(mapMarker);
                }

                hideProgressDialog();
            }
        }
    }

    private void removeMarkers(ArrayList<String> rentalsToRemove) {
        Iterator it = markerRentalMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (rentalsToRemove.contains((String)pair.getValue())) {
                ((Marker)pair.getKey()).remove();
                it.remove();
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        FetchArea fetchArea = new FetchArea(map);
        FilterParams.INSTANCE.params.put("latitude", Double.toString(fetchArea.centerPoint.latitude));
        FilterParams.INSTANCE.params.put("longitude", Double.toString(fetchArea.centerPoint.longitude));
        FilterParams.INSTANCE.params.put("radius", Integer.toString(fetchArea.radiusInMiles));
        AppEventBus.post(new MapChangedEvent());
    }

}
