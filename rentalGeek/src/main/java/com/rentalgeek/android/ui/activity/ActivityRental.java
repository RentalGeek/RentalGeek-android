package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.ui.fragment.FragmentRental;

public class ActivityRental extends GeekBaseActivity {

    private static final String TAG = ActivityFavoriteRentals.class.getSimpleName();
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
}
