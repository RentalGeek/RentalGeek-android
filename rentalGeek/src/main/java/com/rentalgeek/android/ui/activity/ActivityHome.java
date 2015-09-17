package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.mvp.home.HomePresenter;
import com.rentalgeek.android.mvp.home.HomeView;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;
import com.rentalgeek.android.utils.CosignerInviteCaller;

public class ActivityHome extends GeekBaseActivity implements Container<ViewPager>, HomeView {

    private static String TAG = ActivityHome.class.getSimpleName();
    private HomePresenter presenter;
    private MapView mapView;
    private RentalListView rentalListView;

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
        rentalListView = new FragmentRentalListView();

        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.container);

        if( viewPager != null ) {
            setupContainer(viewPager);
        }

        tabLayout.setupWithViewPager(viewPager);

        presenter = new HomePresenter(this);
        presenter.getRentalOfferings(null);

        // silently fetch cosigner invites to know which page to go to
        if (SessionManager.Instance.getCurrentUser() != null) {
            new CosignerInviteCaller(this, false).fetchCosignerInvites();
        }
    }
    
    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment((FragmentMap)mapView,"Map View");
        adapter.addFragment((FragmentRentalListView)rentalListView,"List View");
        container.setAdapter(adapter);
    }

    @Override
    public void setRentals(Rental[] rentals) {
        mapView.setRentals(rentals);
        rentalListView.setRentals(rentals);
    }
}
