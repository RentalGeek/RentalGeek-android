package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentCosignerApp2;

public class ActivityCosignerApp2 extends GeekBaseActivity {

    public ActivityCosignerApp2() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();
        setMenuItemSelected(R.id.cosigner);

        if (savedInstanceState == null) {
            FragmentCosignerApp2 fragment = new FragmentCosignerApp2();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
