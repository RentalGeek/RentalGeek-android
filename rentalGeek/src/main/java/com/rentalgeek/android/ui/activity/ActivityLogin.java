package com.rentalgeek.android.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ShowHomeEvent;
import com.rentalgeek.android.bus.events.ShowRegistrationEvent;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.utils.Analytics;

public class ActivityLogin extends GeekBaseActivity {

    public ActivityLogin() {
        super(false, false, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        if (savedInstanceState == null) {
            Fragment fragment = new FragmentSignIn();
            Bundle args = getIntent().getExtras();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment,"FragmentSignIn").commit();
        }

        setTabs(false);
        setupNavigation();

        Analytics.instance(this).track("LoginActivity - onCreate called");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppEventBus.post(new LoginResult(data, resultCode, requestCode));
    }

    public void onEventMainThread(ShowRegistrationEvent event) {
        Navigation.navigateActivity(this,ActivityRegistration.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onEventMainThread(ShowHomeEvent event) {
        Navigation.navigateActivity(this,ActivityHome.class,true);
    }

}
