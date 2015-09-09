package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentListViewDetails;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.mvp.home.HomePresenterImpl;
import com.rentalgeek.android.mvp.home.HomeView;

public class ActivityHome extends GeekBaseActivity implements Container<ViewPager>, HomeView {

    private static String TAG = "ActivityHome";
    private HomePresenterImpl presenter;
    private FragmentMap mapFragment;

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

        mapFragment = new FragmentMap();

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);

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
        adapter.addFragment(mapFragment,"Map View");
        adapter.addFragment(new FragmentListViewDetails(),"List View");
        container.setAdapter(adapter);
    }

    @Override
    public void setRentals(Rental[] rentals) {
        mapFragment.setRentals(rentals);
    }
}
