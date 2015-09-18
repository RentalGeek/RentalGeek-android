package com.rentalgeek.android.ui.activity;


import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentPaymentConfirmation;

public class ActivityPaymentConfirmation extends GeekBaseActivity {

    private static final String TAG = ActivityPaymentConfirmation.class.getSimpleName();

    public ActivityPaymentConfirmation() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        if (savedInstanceState == null) {
            FragmentPaymentConfirmation fragment = new FragmentPaymentConfirmation();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }
}
