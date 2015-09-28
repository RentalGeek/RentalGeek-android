package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;

public class ActivityCreateProfile extends GeekBaseActivity {

    private static final String TAG = ActivityCreateProfile.class.getSimpleName();

    public ActivityCreateProfile() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_with_fragment);
    }

}
