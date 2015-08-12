package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.utils.StaticClass;

public class ApiManager {

    // Default host
    public static String API_HOST = "http://104.236.100.21";

    public static String regis_link = API_HOST + "/applicants.json";

    public static String getApplicants(String uid) {
        String url = API_HOST + "/v1/applicants/" + uid;
        return url;
    }

    public static String getPropertySearchUrl(String location) {
        String url = API_HOST + "/v1/rental_offerings.json?" + location;
        return url;
    }

    public static String getApplyUrl() {
        String url = API_HOST + "/v2/applies";
        return url;
    }

    public static String getStarredPrpoertiesUrl(String starredPropertyId) {
        String url = API_HOST + "/v1/starred_properties";
        if (TextUtils.isEmpty(starredPropertyId)) return url;
        return String.format("%s/%s", url, starredPropertyId);
    }

    public static String getAddProvider(String providerId) {
        String url = API_HOST + "/v1/sessions/add_providers";
        return url;
    }

    public static String getApplicantPassword() {
        String url = API_HOST + "/applicants/password";
        return url;
    }

    public static String getSignin() {
        String url = API_HOST + "/applicants/sign_in.json";
        return url;
    }

    public static String getTransactions() {
        String url = API_HOST + "/v1/transactions";
        return url;
    }

    public static String getProfile(String id) {
        String url = API_HOST + "/v1/profiles";
        if (TextUtils.isEmpty(id)) return url;
        return String.format("%s/%s", url, id);
    }
}
