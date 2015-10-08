package com.rentalgeek.android.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.utils.Analytics;

public class ActivityLogin extends GeekBaseActivity {

    private static final String TAG = ActivityLogin.class.getSimpleName();

    private FragmentSignIn fragment;

    public ActivityLogin() {
        super(false, false, false);
    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FragmentSignIn.RC_SIGN_IN) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (data != null && data.getAction() != null && data.getAction().equals("com.linkedin.thirdparty.authorize.RESULT_ACTION")) {
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

}
