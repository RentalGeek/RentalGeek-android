package com.rentalgeek.android.api;


import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.preference.AppPreferences;

public enum SessionManager {

    Instance;

    private LoginBackend.User currentUser;

    public LoginBackend.User getCurrentUser() {
        return currentUser;
    }

    public void onUserLoggedIn(LoginBackend.User user) {

        currentUser = user;
        AppPreferences.setAuthToken(user.authentication_token);

        AppPrefes appPref = new AppPrefes(RentalGeekApplication.context, "rentalgeek");
        String appid = String.valueOf(user.id);

        appPref.SaveData("norm_log", "true");
        appPref.SaveData("Uid", appid);
        appPref.SaveData("email", user.email);

        if (user.payment) {
            appPref.SaveIntData("payed", 200);
        }

        if (user.profile_id != null) {
            appPref.SaveData("prof_id", user.profile_id);
        } else {
            appPref.SaveData("prof_id", "");
        }

        appPref.SaveData("first", "logged");

    }

    public void onUserLoggedOut() {
        currentUser = null;
        AppPreferences.setAuthToken(null);
    }

}
