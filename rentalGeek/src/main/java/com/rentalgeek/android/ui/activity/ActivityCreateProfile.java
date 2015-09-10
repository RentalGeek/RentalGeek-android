package com.rentalgeek.android.ui.activity;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.adapter.ProfileFormAdapter;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class ActivityCreateProfile extends GeekBaseActivity implements Validator.ValidationListener {

    private static final String TAG = ActivityCreateProfile.class.getSimpleName();

    NonSwipeableViewPager mPager;
    PageIndicator mIndicator;
    ProfileFormAdapter mAdapter;

    public ActivityCreateProfile() {
        super(false, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mAdapter = new ProfileFormAdapter(getSupportFragmentManager());

        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        mPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //swipeTimer.cancel();
                return false;

            }
        });

        setupNavigation();

    }

    public void flipPager(int position) {
        mPager.setCurrentItem(position, false);
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(View view, Rule<?> rule) {

    }


}
