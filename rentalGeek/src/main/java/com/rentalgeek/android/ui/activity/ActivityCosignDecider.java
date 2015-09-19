package com.rentalgeek.android.ui.activity;

import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.utils.CosignerInviteCaller;

/**
 * Created by rajohns on 9/16/15.
 *
 */
public class ActivityCosignDecider extends GeekBaseActivity {

    public ActivityCosignDecider() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        new CosignerInviteCaller(this, true).fetchCosignerInvites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressDialog(R.string.dialog_msg_loading);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();
    }
}
