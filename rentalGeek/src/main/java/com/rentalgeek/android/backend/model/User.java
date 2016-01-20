package com.rentalgeek.android.backend.model;

import android.text.TextUtils;

import java.lang.reflect.Field;

public class User {

    public String id;
    public String full_name;
    public String first_name;
    public String last_name;
    public String email;
    public String authentication_token;
    public String google;
    public String facebook;
    public String linkedin;
    public boolean payment;
    public String profile_id;
    public String cosigner_profile_id;
    public String roommate_group_id;
    public String completed_lease_id;
    public boolean is_cosigner;
    public boolean allow_push_notifications;

    public boolean hasProfileId() {
        return !TextUtils.isEmpty(profile_id);
    }

    public void setRoommateGroupId(String roommateGroupId) {
        if (TextUtils.isEmpty(roommate_group_id))
            roommate_group_id = roommateGroupId;
    }

    public synchronized void set(String param, Object value) {
        if (param == null || value == null)
            return;

        try {
            Field field = User.class.getDeclaredField(param);

            if (!field.isAccessible())
                field.setAccessible(true);

            field.set(this, value);

            System.out.println(String.format("Setting %s to %s", param, value.toString()));
        } catch (NoSuchFieldException e) {
            System.out.println("Parameter not found");
        } catch (IllegalAccessException e) {
            System.out.println("Illegal access to class");
        }
    }

}
