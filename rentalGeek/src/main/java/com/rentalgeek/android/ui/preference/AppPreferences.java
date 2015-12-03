package com.rentalgeek.android.ui.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.utils.ObscuredSharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class AppPreferences {

    public enum PreferenceMode {
        Temporary, Default
    }

    private static final String TAG = "AppPreferences";

    private static final String SHARED_PREFS_TEMP = "SHARED_PREFS_TEMP";

    // Used to register device for GCM
    private static final String PREF_MSG_SVC_ID_REGISTERED = "PREF_MSG_SVC_ID_REGISTERED";
    private static final String PREF_MSG_SVC_DEVICE_ID = "PREF_MSG_SVC_DEVICE_ID";

    private static final String PREF_LOGIN_DATA = "PREF_LOGIN_DATA";
    private static final String PREF_USERNAME = "PREF_USERNAME";
    private static final String PREF_PASSWORD = "PREF_PASSWORD";

    public static final String PREF_AUTH_TOKEN = "PREF_AUTH_TOKEN";
    public static final String PREF_DATA_MARKETS = "PREF_DATA_MARKETS";

    public static final String PREF_PROFILE = "PREF_PROFILE";
    public static final String PREF_PROFILE_PAGE = "PREF_PROFILE_PAGE";
    public static final String PREF_FIRST_NAME = "PREF_FIRST_NAME";
    public static final String PREF_LAST_NAME = "PREF_LAST_NAME";

    public static final String PREF_SEARCH_MAX_PRICE = "PREF_SEARCH_MAX_PRICE";
    public static final String PREF_SEARCH_SELECTED_BUTTONS = "PREF_SEARCH_SELECTED_BUTTONS";
    public static final String PREF_SEARCH_COMPANY_INDEX = "PREF_SEARCH_COMPANY_INDEX";

    public static void persistLogin(LoginBackend login) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        String loginJson = new Gson().toJson(login);
        editor.putString(PREF_LOGIN_DATA, loginJson);
        editor.commit();
    }

    public static void putFirstName(String first_name) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_FIRST_NAME, first_name);
        editor.commit();
        System.out.println("First name saved");
    }

    public static void putLastName(String last_name) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_LAST_NAME, last_name);
        editor.commit();
        System.out.println("Last name saved");
    }

    public static void putSearchMaxPrice(int max_search_price) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putInt(PREF_SEARCH_MAX_PRICE, max_search_price);
        editor.commit();
    }

    public static void putManagementCompanySelectionIndex(int index) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putInt(PREF_SEARCH_COMPANY_INDEX, index);
        editor.commit();
    }

    public static void putSelectedSearchButtons(ArrayList<Integer> selected_buttons) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();

        Set<String> selected_buttons_set = new HashSet<>();
        for (Integer buttonId : selected_buttons) {
            selected_buttons_set.add(String.valueOf(buttonId));
        }
        editor.putStringSet(PREF_SEARCH_SELECTED_BUTTONS, selected_buttons_set);
        editor.commit();
    }

    public static void putProfile(String profile) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_PROFILE, profile);
        editor.commit();
        System.out.println("Saved profile");
    }

    public static void putProfilePage(String profile_page) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.putString(PREF_PROFILE_PAGE, profile_page);
        editor.commit();
        System.out.println(String.format("Saved profile page %s", profile_page));
    }

    public static void putCosignerProfilePosition(int pageNumber) {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();

        ObscuredSharedPreferences prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences("com.android.rentalgeek", Context.MODE_PRIVATE));
        String email = prefs.getString(ObscuredSharedPreferences.USERNAME_PREF, "");

        editor.putInt(email, pageNumber);
        editor.commit();
    }

    public static void removeProfile() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tempSettings.edit();
        editor.remove(PREF_PROFILE);
        editor.remove(PREF_PROFILE_PAGE);
        editor.remove(PREF_FIRST_NAME);
        editor.remove(PREF_LAST_NAME);
        editor.commit();
    }

    public static LoginBackend getPersistedLogin() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        String loginJson = tempSettings.getString(PREF_LOGIN_DATA, "");
        LoginBackend login = new Gson().fromJson(loginJson, LoginBackend.class);
        return login;
    }

    public static String getFirstName() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_FIRST_NAME, "");
    }

    public static String getLastName() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_LAST_NAME, "");
    }

    public static int getSearchMaxPrice() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getInt(PREF_SEARCH_MAX_PRICE, 1000);
    }

    public static int getManagementCompanySelectionIndex() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getInt(PREF_SEARCH_COMPANY_INDEX, 0);
    }

    public static ArrayList<Integer> getSearchSelectedButtons() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);

        ArrayList<Integer> selectedButtonIds = new ArrayList<>();
        Set<String> selectedButtonStrings = tempSettings.getStringSet(PREF_SEARCH_SELECTED_BUTTONS, new HashSet<String>());
        for (String stringId : selectedButtonStrings) {
            selectedButtonIds.add(Integer.parseInt(stringId));
        }

        return selectedButtonIds;
    }

    public static String getProfile() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_PROFILE, null);
    }

    public static String getProfilePage() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);
        return tempSettings.getString(PREF_PROFILE_PAGE, null);
    }

    public static int getCosignerProfilePosition() {
        final Context context = RentalGeekApplication.context;
        final SharedPreferences tempSettings = context.getSharedPreferences(SHARED_PREFS_TEMP, Context.MODE_PRIVATE);

        ObscuredSharedPreferences prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences("com.android.rentalgeek", Context.MODE_PRIVATE));
        String email = prefs.getString(ObscuredSharedPreferences.USERNAME_PREF, "");

        return tempSettings.getInt(email, 1);
    }

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
