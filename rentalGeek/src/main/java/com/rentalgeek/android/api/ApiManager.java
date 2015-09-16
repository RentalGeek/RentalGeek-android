package com.rentalgeek.android.api;


import android.text.TextUtils;

public class ApiManager {

    // Default host
    private static final String PROD = "http://api.rentalgeek.com/api/v1";
    private static final String STAGE = "http://api.staging.rentalgeek.com/api/v1";

    public static String API_HOST = STAGE;//PROD;

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

    public static String getCosignerItemsUrl() {
        return getApplyUrl() + "?as_cosigner=true";
    }

    public static String getPendingApplicationsUrl() {
        return getApplyUrl() + "?status=pending";
    }

    public static String getApprovedApplicationsUrl() {
        return getApplyUrl() + "?status=approved";
    }

//    public static String getRemoveStarredPropertyUrl(String propertyId) {
//        String url = API_HOST + "/starred_properties/remove_star";
//        if (TextUtils.isEmpty(propertyId)) return url;
//        return String.format("%s/%s", url, propertyId);
//    }
    
    public static String getRental(String rental_id) {
        return String.format("%s/%s/%s",API_HOST,"rental_offerings",rental_id);
    }

    //TODO remove this crap
    public static String getStarredPrpoertiesUrl(String id) {
        return id;
    }
    
    public static String getRentalStar(String rental_id) {
        String url = API_HOST + "/starred_properties";
        if (TextUtils.isEmpty(rental_id)) return url;
        return String.format("%s/%s", url, rental_id);
    }
    
    public static String deleteRentalStar(String star_id) {
        String url = String.format("%s/%s/%s",API_HOST,"starred_properties",star_id);
        return url;
    }

    public static String postRentalStar() {
        String url = API_HOST + "/starred_properties";
        return url;
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

    public static String getRoommateGroups(String id) {
        String url = API_HOST + "/roommate_groups";
        if (TextUtils.isEmpty(id)) return url;
        return String.format("%s/%s", url, id);
    }

    public static String getRoommateInvites(String roommate_group_id ) {
        String url = API_HOST + "/roommate_invites";
        if (TextUtils.isEmpty(roommate_group_id)) return url;
        return String.format("%s/%s", url, roommate_group_id);
    }
}
