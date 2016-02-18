package com.rentalgeek.android.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class GeekProgressDialog {

    private static ProgressDialog progressDialog;

    public static void show(Context context, int messageResourceId) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(context.getResources().getString(messageResourceId));
        progressDialog.show();
    }

    public static void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
