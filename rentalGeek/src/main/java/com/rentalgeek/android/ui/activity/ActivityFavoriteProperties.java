package com.rentalgeek.android.ui.activity;


import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentStarredProperties;

public class ActivityFavoriteProperties extends GeekBaseActionBarActivity {

    private static final String TAG = ActivityFavoriteProperties.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_activity_standard);

        if (savedInstanceState == null) {
            FragmentStarredProperties fragment = new FragmentStarredProperties();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
