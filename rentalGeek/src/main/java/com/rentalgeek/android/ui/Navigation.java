package com.rentalgeek.android.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Navigation {

    public static void navigateActivity(FragmentActivity context, Class activity, Bundle args, boolean clearTop) {
        if (context == null) return;
        final Intent intent = new Intent(context, activity);
        intent.putExtras(args);
        if (clearTop)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void navigateActivity(FragmentActivity activity, Class clazz, boolean clearTop) {
        if (activity == null || activity.getClass().equals(clazz)) return;
        final Intent intent = new Intent(activity, clazz);
        if (clearTop)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void navigateActivity(FragmentActivity context, Class activity) {
        navigateActivity(context, activity, false);
    }

}
