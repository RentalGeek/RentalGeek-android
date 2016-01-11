package com.rentalgeek.android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class ActionAlert {

    public static void show(Context context, String title, String message, String actionText, DialogInterface.OnClickListener actionCallback) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(actionText, actionCallback)
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();
    }

}
