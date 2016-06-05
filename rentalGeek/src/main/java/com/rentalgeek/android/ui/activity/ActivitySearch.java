package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.ShowMessageAlert;
import com.rentalgeek.android.mvp.search.SearchView;
import com.rentalgeek.android.ui.fragment.FragmentSearch;
import com.rentalgeek.android.utils.OkAlert;

public class ActivitySearch extends GeekBaseActivity {

    public ActivitySearch() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);
        setupNavigation();
        SearchView searchView = new FragmentSearch();

        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentSearch) searchView).commit();
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void onEventMainThread(ShowMessageAlert event) {
        if (event.getTitle() != null && event.getMessage() != null) {
            hideProgressDialog();
            OkAlert.show(this, event.getTitle(), event.getMessage());
        }
    }

}
