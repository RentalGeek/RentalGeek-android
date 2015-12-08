package com.rentalgeek.android.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AddMarkersEvent;
import com.rentalgeek.android.bus.events.NoRentalsEvent;
import com.rentalgeek.android.bus.events.SetRentalEvent;
import com.rentalgeek.android.bus.events.SetRentalsEvent;
import com.rentalgeek.android.bus.events.ShowRentalEvent;
import com.rentalgeek.android.model.RentalMarker;
import com.rentalgeek.android.mvp.map.MapPresenter;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.adapter.PlaceAutocompleteAdapter;
import com.rentalgeek.android.ui.view.AutoCompleteAddressListener;
import com.rentalgeek.android.utils.OkAlert;

import java.util.List;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMapClickListener, ClusterManager.OnClusterItemClickListener<RentalMarker>, ClusterManager.OnClusterClickListener<RentalMarker> {

    private GoogleMap map;
    private ClusterManager<RentalMarker> clusterManager;
    private MapPresenter presenter;
    private RentalView rentalView;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private boolean mapPaddingIsAdjusted = false;
    private AutoCompleteTextView locationAutoCompleteTextView;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rentalView = (RentalView) getChildFragmentManager().findFragmentById(R.id.rental);
        presenter = new MapPresenter();

        locationAutoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.location_autocompletetextview);

        locationAutoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d("tagzzz", "pressed enter key");
                    dismissSearchEditText();
                    rentalView.hide();

                    String location = locationAutoCompleteTextView.getText().toString().trim();
                    if (!location.equals("")) {
                        ActivityHome activityHome = (ActivityHome) getActivity();
                        showProgressDialog(R.string.loading_rentals);
                        activityHome.presenter.getRentalOfferings(location);
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
        this.map.setOnMapClickListener(this);
        setUpClusterer();
    }

    private void setUpClusterer() {
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38, 263), 3.0f));
        clusterManager = new ClusterManager<>(getActivity(), this.map);
//        clusterManager.setRenderer(new RentalRenderer());
        this.map.setOnCameraChangeListener(clusterManager);
        this.map.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
    }

    @Override
    public void setRentals(Rental[] rentals) {
        presenter.addRentals(rentals);
    }

    @Override
    public void boundbox(List<RentalMarker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (RentalMarker marker : markers) {
            builder.include(marker.getPosition());
        }

        int width = RentalGeekApplication.getScreenWidth();
        int height = RentalGeekApplication.getScreenHeight();
        int mapPadding = (int) RentalGeekApplication.getDimension(R.dimen.map_padding);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, mapPadding));
    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        final int bottomPreviewHeight = (int)getActivity().getResources().getDimension(R.dimen.img_height);

        // Have to jump through some hoops here with setting the padding to make sure the animateCamera
        // call has desired padding at its beginning
        map.setPadding(0, 0, 0, bottomPreviewHeight);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));

        if (!mapPaddingIsAdjusted) {
            map.setPadding(0, 0, 0, 0);
            animateMapPadding(0, bottomPreviewHeight);
        }
    }

    private void animateMapPadding(int startingBottomPadding, int endingBottomPadding) {
        ValueAnimator animator = ValueAnimator.ofInt(startingBottomPadding, endingBottomPadding);
        final int animationTime = getActivity().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        animator.setDuration(animationTime);

        if (movingUp(startingBottomPadding, endingBottomPadding)) {
            mapPaddingIsAdjusted = true;
            animator.setInterpolator(decelerateInterpolator);
        } else {
            mapPaddingIsAdjusted = false;
            animator.setInterpolator(accelerateInterpolator);
        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                map.setPadding(0, 0, 0, Integer.parseInt(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
    }

    private boolean movingUp(int begin, int end) {
        return end > begin;
    }

    @Override
    public void onMapClick(LatLng position) {
        final int bottomPreviewHeight = (int)getActivity().getResources().getDimension(R.dimen.img_height);

        if (mapPaddingIsAdjusted) {
            animateMapPadding(bottomPreviewHeight, 0);
        }

        rentalView.hide();
        focusOnMap();
    }

    @Override
    public boolean onClusterItemClick(RentalMarker rentalMarker) {
        String rental_id = rentalMarker.getRental().getId();
        presenter.getRental(rental_id);
        focusOnMap();
        return true;
    }

    @Override
    public boolean onClusterClick(Cluster<RentalMarker> cluster) {
        focusOnMap();
        return true;
    }

    private void focusOnMap() {
        locationAutoCompleteTextView.clearFocus();
        dismissSearchEditText();
    }

////     Used for clustering for custom markers
//    private class RentalRenderer extends DefaultClusterRenderer<RentalMarker> {
//        public RentalRenderer() {
//            super(getActivity().getApplicationContext(), map, clusterManager);
//        }
//
//        @Override
//        protected void onClusterItemRendered(RentalMarker rentalMarker, Marker marker) {
//            super.onClusterItemRendered(rentalMarker, marker);
//        }
//    }

    private void dismissSearchEditText() {
        locationAutoCompleteTextView.clearFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(locationAutoCompleteTextView.getWindowToken(), 0);
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

    public void onEventMainThread(NoRentalsEvent event) {
        hideProgressDialog();
        OkAlert.show(getActivity(), "No Results", "No properties were found for that location.");
    }

    public void onEventMainThread(AddMarkersEvent event) {
        if (event.getMarkers() != null) {
            if (map != null) {
                for (RentalMarker rentalMarker : event.getMarkers()) {
                    clusterManager.addItem(rentalMarker);
                }

                boundbox(event.getMarkers());
                hideProgressDialog();
            }
        }
    }

}
