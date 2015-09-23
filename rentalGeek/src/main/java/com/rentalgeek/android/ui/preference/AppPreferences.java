package com.rentalgeek.android.ui.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.rentalgeek.android.RentalGeekApplication;


public class AppPreferences {

    public enum PreferenceMode {
        Temporary, Default
    }

    private static final String TAG = "AppPreferences";

    private static final String SHARED_PREFS_TEMP = "SHARED_PREFS_TEMP";

    // Used to register device for GCM
    private static final String PREF_MSG_SVC_ID_REGISTERED = "PREF_MSG_SVC_ID_REGISTERED";
    private static final String PREF_MSG_SVC_DEVICE_ID = "PREF_MSG_SVC_DEVICE_ID";

    private static final String PREF_USERNAME = "PREF_USERNAME";
    private static final String PREF_PASSWORD = "PREF_PASSWORD";

    public static final String PREF_AUTH_TOKEN = "PREF_AUTH_TOKEN";
    public static final String PREF_DATA_MARKETS = "PREF_DATA_MARKETS";



    public static boolean getMessageServiceFirstRun() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getBoolean(PREF_MSG_SVC_ID_REGISTERED, true);
    }

    public static void setMessageServiceFirstRun() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putBoolean(PREF_MSG_SVC_ID_REGISTERED, false);
        editor.commit();
    }

    public static boolean setUserName(String userName) {
        //final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_USERNAME, userName);
        final boolean success = editor.commit();
        return success;
    }

    public static String getUserName() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_USERNAME, "");
    }

    public static boolean setPassword(String passWord) {
        //final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_PASSWORD, passWord);
        final boolean success = editor.commit();
        return success;
    }

    public static String getPassword() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_PASSWORD, "");
    }

    public static boolean setAuthToken(String token) {
        //final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_AUTH_TOKEN, token);
        final boolean success = editor.commit();
        return success;
    }

    public static String getAuthToken() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_AUTH_TOKEN, "");
    }

    public static boolean setMessageServiceDeviceId(String id) {
        //final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_MSG_SVC_DEVICE_ID, id);
        final boolean success = editor.commit();
        return success;
    }

    public static String getMessageServiceDeviceId() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_MSG_SVC_DEVICE_ID, "");
    }
}
