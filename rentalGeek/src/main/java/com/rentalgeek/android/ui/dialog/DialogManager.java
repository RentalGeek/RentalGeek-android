package com.rentalgeek.android.ui.dialog;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.rentalgeek.android.R;


public class DialogManager {

    public static void showCrouton(AppCompatActivity activity, String croutonText) {

        View coordinatorLayoutView = activity.findViewById(R.id.snackbarPosition);

        if (coordinatorLayoutView != null) {
            Snackbar.make(coordinatorLayoutView, Html.fromHtml(croutonText), Snackbar.LENGTH_LONG)
                            //.setAction(R.string.snackbar_action_undo, clickListener)
                    .show();
        }

    }

    public static void showCrouton(AppCompatActivity activity, SpannableStringBuilder croutonText) {

        View coordinatorLayoutView = activity.findViewById(R.id.snackbarPosition);

        if (coordinatorLayoutView != null) {
            Snackbar.make(coordinatorLayoutView, croutonText, Snackbar.LENGTH_LONG)
                    //.setAction(R.string.snackbar_action_undo, clickListener)
                    .show();
        }

    }
}
