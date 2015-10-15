package com.rentalgeek.android.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.utils.Analytics;

public class ActivityLogin extends GeekBaseActivity {

    private static final String TAG = ActivityLogin.class.getSimpleName();

    public ActivityLogin() {
        super(false, false, false);
    }

    FragmentSignIn fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        if (savedInstanceState == null) {
            fragment = new FragmentSignIn();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }

        setTabs(false);
        setupNavigation();

        Analytics.instance(this).track("LoginActivity - onCreate called");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if( fragment != null ) {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
