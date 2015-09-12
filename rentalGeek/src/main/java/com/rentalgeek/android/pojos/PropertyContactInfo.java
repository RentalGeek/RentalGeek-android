package com.rentalgeek.android.pojos;

import android.telephony.PhoneNumberUtils;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class PropertyContactInfo {

    private String name;
    private String email;
    private String phoneNumber;

    public PropertyContactInfo(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFormattedPhoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = "";
        }

        return PhoneNumberUtils.formatNumber(phoneNumber, "US");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
