package com.rentalgeek.android.ui.dialog.manager;


import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class AppDialog extends Dialog {

    public AppDialog(Context context, int theme) {
        super(context, theme);
    }

    public static void showMessageDialog(FragmentActivity activity, Fragment fragment) {


    }

}
