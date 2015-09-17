package com.rentalgeek.android.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rentalgeek.android.utils.CosignerInviteCaller;

/**
 * Created by rajohns on 9/16/15.
 *
 */
public class ActivityCosignDecider extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CosignerInviteCaller(this, true).fetchCosignerInvites();
        finish();
    }

}
