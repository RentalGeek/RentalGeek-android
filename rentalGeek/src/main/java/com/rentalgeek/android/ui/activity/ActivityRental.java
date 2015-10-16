package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;

import com.rentalgeek.android.bus.events.ShowCosignApplicationEvent;
import com.rentalgeek.android.ui.Navigation;

import com.rentalgeek.android.mvp.rental.RentalView;

import com.rentalgeek.android.ui.fragment.FragmentRental;

import com.rentalgeek.android.bus.events.ShowProfileCreationEvent;

public class ActivityRental extends GeekBaseActivity {

    private static final String TAG = ActivityRental.class.getSimpleName();
    private RentalView rentalView;

    public ActivityRental() {
        super(true, true, true);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);
        setupNavigation();

        if (savedInstanceState == null) {
            rentalView = new FragmentRental();

            Bundle args = getIntent().getExtras();
            ((FragmentRental)rentalView).setFullView(true);
            ((FragmentRental)rentalView).setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentRental)rentalView).commit();
        }
    }

    public void onEventMainThread(ShowProfileCreationEvent event) {
        Navigation.navigateActivity(this, ActivityCreateProfile.class);
    }

    public void onEventMainThread(ShowCosignApplicationEvent event) {
        Navigation.navigateActivity(this, ActivityCosignDecider.class);
    }
}
