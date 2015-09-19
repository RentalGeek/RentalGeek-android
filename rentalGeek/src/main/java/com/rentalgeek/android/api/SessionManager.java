package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.UserProfile;
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.backend.model.User;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import java.util.List;

public enum SessionManager {

    Instance;

    private User currentUser;
    private List<Profile> profiles;

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasCompletedLeaseId() {
        if (currentUser != null) {
            return !TextUtils.isEmpty(currentUser.completed_lease_id);
        }
        return false;
    }

    public boolean hasPayed() {
        if (currentUser != null) {
            return currentUser.payment;
        }
        return false;
    }

    public boolean hasGeekScore() {
        Profile profile = getDefaultProfile();
        if (profile != null) {
            return !TextUtils.isEmpty(profile.geek_score);
        }
        return false;
    }

    public String getGeekScore() {
        Profile profile = getDefaultProfile();
        if (profile != null) {
            return profile.geek_score;
        }
        return null;
    }

    public Profile getDefaultProfile() {
        if (!ListUtils.isNullOrEmpty(profiles)) {
            return profiles.get(0);
        }
        return null;
    }

    public boolean hasProfile() {
        return getDefaultProfile() != null;
    }

    public String getDefaultProfileId() {
        Profile profile = getDefaultProfile();
        if (profile == null) return null;
        return profile.id;
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

//        if (currentUser.profile_id != null) {
//            appPref.SaveData("prof_id", currentUser.profile_id);
//        } else {
//            appPref.SaveData("prof_id", "");
//        }

        appPref.SaveData("first", "logged");

    }

    public void onUserLoggedOut() {
        currentUser = null;
        AppPreferences.setAuthToken(null);
    }

}
