package com.rentalgeek.android.ui.activity;

import android.util.Log;
import android.os.Bundle;
import com.rentalgeek.android.R;
import android.support.v4.view.ViewPager;
import com.rentalgeek.android.pojos.Rental;
import android.support.v4.app.FragmentManager;
import com.rentalgeek.android.ui.home.HomeView;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.ui.home.HomePresenterImpl;
import com.rentalgeek.android.ui.fragment.FragmentListViewDetails;

public class ActivityHome extends GeekBaseActivity implements Container<ViewPager>, HomeView {

    private static String TAG = "ActivityHome";
    private HomePresenterImpl presenter;
    private FragmentMap mapFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub,R.layout.pager_container);
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
