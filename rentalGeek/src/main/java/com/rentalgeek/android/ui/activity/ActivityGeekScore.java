package com.rentalgeek.android.ui.activity;


import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentGeekScore;

public class ActivityGeekScore extends GeekBaseActivity {

    private static final String TAG = ActivityGeekScore.class.getSimpleName();

    public ActivityGeekScore() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        if (savedInstanceState == null) {
            FragmentGeekScore fragment = new FragmentGeekScore();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}
