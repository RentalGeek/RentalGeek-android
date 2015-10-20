package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.BuildConfig;

public class ApiManager {

    // Default host
    private static final String PROD = "http://api.rentalgeek.com/api/v1";
    private static final String STAGE = "http://api.staging.rentalgeek.com/api/v1";

    private static final String GOOGLE_MAPS = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyDuVB1GHSKyz51m1w4VGs_XTyxVlK01INY";

    public static String API_HOST = BuildConfig.DEBUG ? STAGE : PROD;

    public static String regis_link = API_HOST + "/users.json";

    // "api/"
    public static String getApplicants(String uid) {
        return API_HOST + "/users/" + uid;
    }

    public static String getPropertySearchUrl() {
        return API_HOST + "/rental_offerings.json";
    }

    public static String getPropertySearchUrl(String parameters) {
        return String.format("%s%s%s", API_HOST, "/rental_offerings.json?", parameters);
    }

    public static String getFavoritesUrl() {
        return String.format("%s/%s", API_HOST, "rental_offerings?starred=true");
    }

    public static String getApplyUrl() {
        return API_HOST + "/applications";
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

    public static String cosignerInvitesUrl() {
        return API_HOST + "/cosigner_invites";
    }

    public static String sentCosignerInvites() {
        return cosignerInvitesUrl() + "?as_inviter=true";
    }

    public static String deleteCosignerInvite(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId;
    }

    public static String propertyPhotos(String rentalOfferingId) {
        return API_HOST + "/property_photos?rental_offering_id=" + rentalOfferingId;
    }

    public static String cosignerProfilesUrl(String cosignerProfileId) {
        return API_HOST + "/cosigner_profiles/" + cosignerProfileId;
    }

    public static String specificUserUrl(String userId) {
        return API_HOST + "/users/" + userId;
    }

    public static String deleteProfile(String userId) {
        return API_HOST + "/profiles/" + userId;
    }

    public static String signLeaseUrl(int leaseId) {
        return API_HOST + "/leases/" + leaseId + "/embedded_signature_url";
    }

    public static String getAcceptCosignerInviteUrl(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId + "/accept";
    }

    public static String getDenyCosignerInviteUrl(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId + "/deny";
    }

//    public static String getRemoveStarredPropertyUrl(String propertyId) {
//        String url = API_HOST + "/starred_properties/remove_star";
//        if (TextUtils.isEmpty(propertyId)) return url;
//        return String.format("%s/%s", url, propertyId);
//    }

    public static String getRental(String rental_id) {
        return String.format("%s/%s/%s", API_HOST, "rental_offerings", rental_id);
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
        return String.format("%s/%s/%s", API_HOST, "starred_properties", star_id);
    }

    public static String postRentalStar() {
        return API_HOST + "/starred_properties";
    }

    public static String getAddProvider(String providerId) {
        return API_HOST + "/sessions/add_providers";
    }

    public static String getApplicantPassword() {
        return API_HOST + "/users/password";
    }

    public static String getSignin() {
        return API_HOST + "/users/sign_in.json";
    }

    public static String getSignOut() {
        return API_HOST + "/users/sign_out.json";
    }

    public static String getTransactions() {
        return API_HOST + "/transactions";
    }

    public static String getPayments() {
        return API_HOST + "/payments";
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

    public static String getRoommateGroupRemoveUser(String groupId, String userId) {
        String url = API_HOST + "/roommate_groups";
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) return null;
        return String.format("%s/%s/remove_user?user_id=%s", url, groupId, userId);
    }

    public static String getRoommateInvites(String roommate_group_id) {
        String url = API_HOST + "/roommate_invites";
        if (TextUtils.isEmpty(roommate_group_id)) return url;
        return String.format("%s/%s", url, roommate_group_id);
    }

    public static String getRoommateInviteAccept(String inviteId) {
        if (TextUtils.isEmpty(inviteId)) return null;
        String url = API_HOST + "/roommate_invites";
        return String.format("%s/%s/accept", url, inviteId);
    }

    public static String getRoommateInviteDeny(String inviteId) {
        if (TextUtils.isEmpty(inviteId)) return null;
        String url = API_HOST + "/roommate_invites";
        return String.format("%s/%s/deny", url, inviteId);
    }

    public static String getLease(String leaseId) {
        String url = API_HOST + "/leases";
        if (TextUtils.isEmpty(leaseId)) return null;
        return String.format("%s/%s", url, leaseId);
    }

    public static String getLeaseRoommatePayments(String leaseId) {
        String url = API_HOST + "/leases";
        if (TextUtils.isEmpty(leaseId)) return null;
        return String.format("%s/%s/roommate_payments", url, leaseId);
    }

    public static String postApplication() {
        return String.format("%s/%s", API_HOST, "applications");
    }

    public static String getFullAddress(String place_id) {
        return String.format("%s?placeid=%s&sensor=false&key=%s", GOOGLE_MAPS, place_id, GOOGLE_MAPS_API_KEY);
    }

}
