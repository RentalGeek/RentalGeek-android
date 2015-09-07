package com.rentalgeek.android.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog.AppDialogFragment;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog.AppDialogListener;


public class GeekDialogFragment extends AppDialogFragment {

    public GeekDialogFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        final AppDialogListener listener = getDialogListener();
        final Bundle args = getArguments();
        listener.onDialogButtonClick(this, DialogInterface.BUTTON_NEGATIVE, args);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity context = getActivity();
        final Bundle args = getArguments();
        ProgressDialog progressDialog = new ProgressDialog(context);//, R.style.GeekProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        int messageResId = args.getInt(Common.DIALOG_MSG_ID, R.string.dialog_msg_loading);
        progressDialog.setMessage(getText(messageResId));
        return progressDialog;
    }
}
