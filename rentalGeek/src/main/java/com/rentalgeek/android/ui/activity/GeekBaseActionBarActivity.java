package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.dialog.AppProgressDialog;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog;


public class GeekBaseActionBarActivity extends ActionBarActivity {


    /*****************************
     * Progress Dialog
     *
     *****************************/

    protected void hideProgressDialog() {
        GeekDialog.dismiss(this, AppProgressDialog.class);
    }

    protected void showProgressDialog(int messageResId) {
        Bundle args = new Bundle();
        args.putInt(Common.DIALOG_MSG_ID, messageResId);
        AppEventBus.post(new AppDialogRequestEvent<AppProgressDialog>(AppProgressDialog.class, args, null, false));
        //CargomaticDialog.showDialog(this, CargoProgressDialog.class, args, null);
    }

}
