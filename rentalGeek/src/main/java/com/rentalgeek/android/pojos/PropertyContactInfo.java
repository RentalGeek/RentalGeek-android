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
        if (name == null) {
            return "Property name: N/A";
        }

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        if (email == null) {
            return "Email: N/A";
        }

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormattedPhoneNumber() {
        if (phoneNumber == null) {
            return "Phone number: N/A";
        }

        return PhoneNumberUtils.formatNumber(phoneNumber, "US");
    }

}
