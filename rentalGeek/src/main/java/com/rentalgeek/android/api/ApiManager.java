package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.backend.LoginBackend;

public class ApiManager {

    public static LoginBackend.user currentUser;
    // Default host
    private static final String PROD = "http://api.rentalgeek.com/api/v1";
    private static final String STAGE = "http://staging.rentalgeek.com/api/v1";

    public static String API_HOST = PROD;

    public static String regis_link = API_HOST + "/users.json";
    // "api/"
    public static String getApplicants(String uid) {
        String url = API_HOST + "/users/" + uid;
        return url;
    }

    public static String getPropertySearchUrl(String location) {
        String url = API_HOST + "/rental_offerings.json?" + location;
        return url;
    }

    public static String getApplyUrl() {
        String url = API_HOST + "/applications";
        return url;
    }

//    public static String getRemoveStarredPropertyUrl(String propertyId) {
//        String url = API_HOST + "/starred_properties/remove_star";
//        if (TextUtils.isEmpty(propertyId)) return url;
//        return String.format("%s/%s", url, propertyId);
//    }

    public static String getStarredPrpoertiesUrl(String starredPropertyId) {
        String url = API_HOST + "/starred_properties";
        if (TextUtils.isEmpty(starredPropertyId)) return url;
        return String.format("%s/%s", url, starredPropertyId);
    }

    public static String getAddProvider(String providerId) {
        String url = API_HOST + "/sessions/add_providers";
        return url;
    }

    public static String getApplicantPassword() {
        String url = API_HOST + "/users/password";
        return url;
    }

    public static String getSignin() {
        String url = API_HOST + "/users/sign_in.json";
        return url;
    }

    public static String getSignOut() {
        String url = API_HOST + "/users/sign_out.json";
        return url;
    }

    public static String getTransactions() {
        String url = API_HOST + "/transactions";
        return url;
    }

    public static String getProfile(String id) {
        String url = API_HOST + "/profiles";
        if (TextUtils.isEmpty(id)) return url;
        return String.format("%s/%s", url, id);
    }

}
