package com.rentalgeek.android.ui.activity;


import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentRoommates;

public class ActivityRoommates extends GeekBaseActivity {

    public ActivityRoommates() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);
        setupNavigation();
        setMenuItemSelected(R.id.roommates);

        if (savedInstanceState == null) {
            FragmentRoommates fragment = new FragmentRoommates();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
