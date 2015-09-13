package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentPropertiesApplied;
import com.rentalgeek.android.ui.fragment.FragmentPropertiesApproved;

/**
 * Created by rajohns on 9/12/15.
 *
 */
public class ActivityProperties extends GeekBaseActivity implements Container<ViewPager> {

    ViewPager viewPager;
    private int numFragmentsLoading = 0;

    public ActivityProperties() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub, R.layout.swipe_pager_container);
        setTabs(true);
        setupNavigation();

        viewPager = (ViewPager)findViewById(R.id.container);
        if (viewPager != null) {
            setupContainer(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPropertiesApplied(), "Applied/Pending");
        adapter.addFragment(new FragmentPropertiesApproved(), "Approved");
        container.setAdapter(adapter);
    }

    public int getLoadingCount() {
        return numFragmentsLoading;
    }

    public void incrementLoadingCount() {
        numFragmentsLoading++;
    }

    public void decrementLoadingCount() {
        numFragmentsLoading--;
    }

}
