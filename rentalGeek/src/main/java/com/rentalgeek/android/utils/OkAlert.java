package com.rentalgeek.android.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class OkAlert {

    public static void show(Context context, String title, String message) {
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .show();
    }

    public static void showUnknownError(Context context) {
        show(context, "Unknown Error", "An unknown error has occurred.");
    }

}
