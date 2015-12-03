package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentApprovedApplications;
import com.rentalgeek.android.ui.fragment.FragmentPendingApplications;

/**
 * Created by rajohns on 9/12/15.
 */
public class ActivityApplications extends GeekBaseActivity implements Container<ViewPager> {

    ViewPager viewPager;

    public ActivityApplications() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub, R.layout.swipe_pager_container);
        setTabs(true);
        setupNavigation();
        setMenuItemSelected(R.id.applications);

        viewPager = (ViewPager) findViewById(R.id.container);
        if (viewPager != null) {
            setupContainer(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPendingApplications(), "Applied/Pending");
        adapter.addFragment(new FragmentApprovedApplications(), "Approved");
        container.setAdapter(adapter);
    }

}
