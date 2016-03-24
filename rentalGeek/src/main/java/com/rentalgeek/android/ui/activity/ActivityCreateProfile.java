package com.rentalgeek.android.ui.activity;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.SubmitProfileEvent;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.adapter.ProfileFormAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;

import static com.rentalgeek.android.constants.IntentKey.*;

public class ActivityCreateProfile extends GeekBaseActivity {

    NonSwipeableViewPager mPager;
    ProfileFormAdapter mAdapter;

    public ActivityCreateProfile() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        boolean resubmittingProfile = false;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resubmittingProfile = extras.getBoolean(RESUBMITTING_PROFILE);
        }

        mAdapter = new ProfileFormAdapter(getSupportFragmentManager(), resubmittingProfile);

        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        setupNavigation();

        if (AppPreferences.getProfilePage() != null) {
            int page = Integer.parseInt(AppPreferences.getProfilePage());
            System.out.println(page);
            flipPager(page);
        }
    }

    public void flipPager(int position) {
        mPager.setCurrentItem(position, false);
    }

    public void onEventMainThread(SubmitProfileEvent event) {
        if (event.resubmitting) {
            Navigation.navigateActivity(this, ActivityGeekScore.class);
        } else {
            Navigation.navigateActivity(this, ActivityNeedPayment.class);
        }
    }

}
