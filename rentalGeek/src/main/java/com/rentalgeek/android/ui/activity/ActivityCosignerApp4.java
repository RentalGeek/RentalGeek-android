package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentCosignerApp4;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class ActivityCosignerApp4 extends GeekBaseActivity {

    public ActivityCosignerApp4() {
        super(true, true, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        if (savedInstanceState == null) {
            FragmentCosignerApp4 fragment = new FragmentCosignerApp4();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}