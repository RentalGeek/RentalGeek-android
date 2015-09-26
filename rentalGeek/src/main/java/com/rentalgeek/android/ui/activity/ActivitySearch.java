package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import android.content.Intent;

import com.rentalgeek.android.R;

import com.rentalgeek.android.ui.Navigation;

import com.rentalgeek.android.mvp.search.SearchView;

import com.rentalgeek.android.bus.events.SearchEvent;

import com.rentalgeek.android.ui.fragment.FragmentSearch;

import android.support.v4.widget.DrawerLayout;

import android.app.Activity;

public class ActivitySearch extends GeekBaseActivity {

    private static final String TAG = ActivitySearch.class.getSimpleName();
    private SearchView searchView; 

    public ActivitySearch() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        searchView = new FragmentSearch();

        if (savedInstanceState == null) {

            Bundle args = getIntent().getExtras();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentSearch)searchView).commit();
        }
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    
    public void onEventMainThread(SearchEvent event) {

        if( event.getBundle() != null ) {
            Navigation.navigateActivity(this,ActivityHome.class,event.getBundle(),true);
        }
    }

}
