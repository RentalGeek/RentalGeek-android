package com.rentalgeek.android.utils;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.User;

public final class Analytics {

    public static final String token = "3c61141d84771e0c6bcfc0f84fb8db95";

    public static MixpanelAPI instance(Context context) {
        return MixpanelAPI.getInstance(context, token);
    }

    public static void logUserLogin(Context context) {
        MixpanelAPI mixpanel = instance(context);
        User currentUser = SessionManager.Instance.getCurrentUser();

        if (currentUser != null) {
            mixpanel.getPeople().identify(currentUser.id);
            updateIfNonNull(mixpanel, "full_name", currentUser.full_name);
            updateIfNonNull(mixpanel, "first_name", currentUser.first_name);
            updateIfNonNull(mixpanel, "last_name", currentUser.last_name);
            updateIfNonNull(mixpanel, "email", currentUser.email);
            mixpanel.getPeople().set("payment", currentUser.payment);
            updateIfNonNull(mixpanel, "profile_id", currentUser.profile_id);
            updateIfNonNull(mixpanel, "cosigner_profile_id", currentUser.cosigner_profile_id);
            updateIfNonNull(mixpanel, "roommate_group_id", currentUser.roommate_group_id);
            updateIfNonNull(mixpanel, "completed_lease_id", currentUser.completed_lease_id);
            mixpanel.getPeople().set("is_cosigner", currentUser.is_cosigner);
            mixpanel.getPeople().set("allow_push_notifications", currentUser.allow_push_notifications);
            mixpanel.getPeople().increment("number_of_logins", 1);
        }
    }

    private static void updateIfNonNull(MixpanelAPI mixPanel, String key, String value) {
        if (key != null && value != null) {
            mixPanel.getPeople().set(key, value);
        }
    }

}
