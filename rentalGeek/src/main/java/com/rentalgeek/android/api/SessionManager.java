package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.UserProfile;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import java.util.List;

public enum SessionManager {

    Instance;

    private LoginBackend.User currentUser;
    private List<UserProfile.Profile> profiles;

    public LoginBackend.User getCurrentUser() {
        return currentUser;
    }

    public boolean hasPayed() {
        if (currentUser != null) {
            return currentUser.payment;
        }
        return false;
    }

    public boolean hasGeekScore() {
        UserProfile.Profile profile = getDefaultProfile();
        if (profile != null) {
            return !TextUtils.isEmpty(profile.geek_score);
        }
        return false;
    }

    public String getGeekScore() {
        UserProfile.Profile profile = getDefaultProfile();
        if (profile != null) {
            return profile.geek_score;
        }
        return null;
    }

    public UserProfile.Profile getDefaultProfile() {
        if (!ListUtils.isNullOrEmpty(profiles)) {
            return profiles.get(0);
        }
        return null;
    }

    public void onUserLoggedIn(LoginBackend login) {

        currentUser = login.user;
        profiles = login.profiles;

        AppPreferences.setAuthToken(currentUser.authentication_token);

        AppPrefes appPref = new AppPrefes(RentalGeekApplication.context, "rentalgeek");
        String appid = String.valueOf(currentUser.id);

        appPref.SaveData("norm_log", "true");
        appPref.SaveData("Uid", appid);
        appPref.SaveData("email", currentUser.email);

        if (currentUser.payment) {
            appPref.SaveIntData("payed", 200);
        }

        if (currentUser.profile_id != null) {
            appPref.SaveData("prof_id", currentUser.profile_id);
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
