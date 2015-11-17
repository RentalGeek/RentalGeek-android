package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;
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

import java.util.List;

public class FragmentMap extends GeekBaseFragment implements OnMapReadyCallback, MapView, OnMapClickListener, ClusterManager.OnClusterItemClickListener<RentalMarker> {

    private GoogleMap map;
    private ClusterManager<RentalMarker> clusterManager;
    private MapPresenter presenter;
    private RentalView rentalView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Waits until map is available for us to use

        rentalView = (RentalView) getChildFragmentManager().findFragmentById(R.id.rental);
        presenter = new MapPresenter();

        return view;
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
    }

    @Override
    public void setRentals(Rental[] rentals) {
        if (map != null) {
//            map.clear();
//            markers.clear();
//            markerRentalMap.clear();
            presenter.addRentals(rentals);
        }
    }

    @Override
    public void boundbox(List<RentalMarker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (RentalMarker marker : markers) {
            builder.include(marker.getPosition());
        }

        int width = RentalGeekApplication.getScreenWidth();
        int mapPadding = (int) RentalGeekApplication.getDimension(R.dimen.map_padding);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, width, mapPadding));
    }

    @Override
    public void zoomTo(double latitude, double longitude, int zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude-0.002, longitude), zoom));
    }

    @Override
    public void onMapClick(LatLng position) {
        rentalView.hide();
    }

    @Override
    public boolean onClusterItemClick(RentalMarker rentalMarker) {
        String rental_id = rentalMarker.getRental().getId();
        presenter.getRental(rental_id);
        return true;
    }

    // Used for clustering for custom markers
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
                    clusterManager.addItem(rentalMarker);
                }

                boundbox(event.getMarkers());
                hideProgressDialog();
            }
        }
    }
}
