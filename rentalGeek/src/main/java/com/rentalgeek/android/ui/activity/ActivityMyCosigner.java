package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentMyCosigner;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class ActivityMyCosigner extends GeekBaseActivity {

    public ActivityMyCosigner() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();
        setMenuItemSelected(R.id.my_cosigner);

        if (savedInstanceState == null) {
            FragmentMyCosigner fragment = new FragmentMyCosigner();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
