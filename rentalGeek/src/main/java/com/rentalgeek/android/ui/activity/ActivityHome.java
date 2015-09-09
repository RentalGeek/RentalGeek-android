package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import com.rentalgeek.android.R;
import android.support.v4.view.ViewPager;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.mvp.home.HomeView;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.mvp.home.HomePresenterImpl;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;
import com.rentalgeek.android.ui.fragment.FragmentListViewDetails;

public class ActivityHome extends GeekBaseActivity implements Container<ViewPager>, HomeView {

    private static String TAG = "ActivityHome";
    private HomePresenterImpl presenter;
    private MapView mapView;

    public ActivityHome() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub, R.layout.pager_container);
        setTabs(true);
        setupNavigation();

        mapView = new FragmentMap();

        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.container);

        if( viewPager != null ) {
            setupContainer(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        presenter = new HomePresenterImpl(this);
        presenter.getRentalOfferings(null);
    }
    
    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment((FragmentMap)mapView,"Map View");
        adapter.addFragment(new FragmentListViewDetails(),"List View");
        container.setAdapter(adapter);
    }

    @Override
    public void setRentals(Rental[] rentals) {
        mapView.setRentals(rentals);
    }
}
