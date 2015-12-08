package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.model.CosignerProfile;
import com.rentalgeek.android.backend.model.CosignerProfileSingleRootDTO;
import com.rentalgeek.android.backend.model.Profile;
import com.rentalgeek.android.backend.model.User;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

import java.util.List;

public enum SessionManager {

    Instance;

    private User currentUser;
    private List<Profile> profiles;
    private CosignerProfile cosignerProfile;

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

    public void setPayed(boolean payed) {
        if (currentUser != null) {
            currentUser.payment = payed;
        }
    }

    public String getGeekScore() {
        Profile profile = getDefaultProfile();

        if (profile != null) {
            return (String)profile.get("geek_score");
        }

        return null;
    }

    public void setDefaultProfile(Profile profile) {
        if (profile != null) {
            System.out.println(profile);

            if (profiles.isEmpty()) {
                profiles.add(0, profile);
            } else {
                profiles.set(0, profile);
            }
        }
    }

    public Profile getDefaultProfile() {
        if (profiles.isEmpty()) {
            profiles.add(0, new Profile());
        }

        return profiles.get(0);
    }

    public boolean hasProfile() {
        String id = (String) getDefaultProfile().get("id");
        return (id != null && !id.isEmpty());
    }

    public CosignerProfile getCosignerProfile() {
        return cosignerProfile;
    }

    public void setCosignerProfile(String cosignerProfileJson) {
        CosignerProfileSingleRootDTO cosignerProfileSingleRootDTO = new Gson().fromJson(cosignerProfileJson, CosignerProfileSingleRootDTO.class);
        cosignerProfile = cosignerProfileSingleRootDTO.cosigner_profile;
    }

    public void onUserLoggedIn(LoginBackend login) {
        currentUser = login.user;
        profiles = login.profiles;
        cosignerProfile = login.cosigner_profiles.size() > 0 ? login.cosigner_profiles.get(0) : null;

        if (profiles.size() == 0) {
            if (AppPreferences.getProfile() != null) {
                Profile profile = GeekGson.getInstance().fromJson(AppPreferences.getProfile(), Profile.class);
                setDefaultProfile(profile);
            }
        }

        AppPreferences.setAuthToken(currentUser.authentication_token);
        AppPreferences.persistLogin(login);

        String first_name = AppPreferences.getFirstName();
        String last_name = AppPreferences.getLastName();

        if (!first_name.isEmpty() && !last_name.isEmpty()) {
            currentUser.first_name = first_name;
            currentUser.last_name = last_name;
        }

        AppPrefes appPref = new AppPrefes(RentalGeekApplication.context, "rentalgeek");
        String appid = String.valueOf(currentUser.id);

        appPref.SaveData("norm_log", "true");
        appPref.SaveData("Uid", appid);
        appPref.SaveData("email", currentUser.email);

        appPref.SaveData("first", "logged");
    }

    public void onUserLoggedOut() {
        currentUser = null;
        AppPreferences.setAuthToken(null);
        AppPreferences.persistLogin(null);
    }

}
