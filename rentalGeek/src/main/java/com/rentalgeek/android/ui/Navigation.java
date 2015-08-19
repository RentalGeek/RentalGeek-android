package com.rentalgeek.android.ui;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public class Navigation {

    private static final String TAG = "Navigation";

    public static void navigateActivity(FragmentActivity context, Class activity, boolean finish) {
        if (context == null) return;
        if (finish) context.finish();
        navigateActivity(context, activity);
    }

    public static void navigateActivity(FragmentActivity context, Class activity) {
        if (context == null) return;
        final Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
