package com.rentalgeek.android.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Navigation {

    private static final String TAG = "Navigation";

    public static void navigateActivity(FragmentActivity context, Class activity, Bundle args, boolean clearTop) {
        if (context == null) return;
        final Intent intent = new Intent(context, activity);
        intent.putExtras(args);
        if (clearTop) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void navigateActivity(FragmentActivity context, Class activity, boolean clearTop) {
        if (context == null) return;
        final Intent intent = new Intent(context, activity);
        if (clearTop) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void navigateActivity(FragmentActivity context, Class activity) {
        navigateActivity(context, activity, false);
    }
}
