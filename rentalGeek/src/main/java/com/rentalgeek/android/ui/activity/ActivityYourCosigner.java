package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentYourCosigner;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class ActivityYourCosigner extends GeekBaseActivity {

    public ActivityYourCosigner() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();
        setMenuItemSelected(R.id.your_cosigner);

        if (savedInstanceState == null) {
            FragmentYourCosigner fragment = new FragmentYourCosigner();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
