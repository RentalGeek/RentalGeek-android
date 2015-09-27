package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

public class ActivityCreateProfile extends GeekBaseActivity {

    private static final String TAG = ActivityCreateProfile.class.getSimpleName();

    public ActivityCreateProfile() {
        super(true, true, true);
    }

    @Override
    public void OnCreate(Bundle bundle) {
        super.onCreate();

        setContentView(R.layout.activity_with_fragment);
    }
}
