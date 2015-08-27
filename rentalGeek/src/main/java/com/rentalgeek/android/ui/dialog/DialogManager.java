package com.rentalgeek.android.ui.dialog;

import android.support.v4.app.FragmentActivity;

import com.rentalgeek.android.ui.view.Crouton;
import com.rentalgeek.android.ui.view.Style;


public class DialogManager {

    public static void showCrouton(FragmentActivity activity, String croutonText) {
        Crouton crouton = Crouton.makeText(activity, croutonText, Style.ALERT);
        crouton.show();
    }
}
