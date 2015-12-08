package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.Navigation;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityNeedPayment extends GeekBaseActivity {

    public ActivityNeedPayment() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_need_payment);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.pay_button)
    public void payButtonTapped(View v) {
        Navigation.navigateActivity(this, ActivityPayment.class);
    }

}
