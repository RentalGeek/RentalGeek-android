package com.rentalgeek.android.api;


import android.text.TextUtils;

import com.rentalgeek.android.BuildConfig;

public class ApiManager {

    private static final String PROD = "http://api.rentalgeek.com";
    private static final String STAGE = "http://api.staging.rentalgeek.com";

    private static final String GOOGLE_MAPS = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyDuVB1GHSKyz51m1w4VGs_XTyxVlK01INY";

    private static final String LINKEDIN_URL = "https://api.linkedin.com/v1/people/~:(first-name,last-name,picture-url,id,email-address)";

    public static String API_HOST = BuildConfig.DEBUG ? STAGE : PROD;

    public static String regis_link = API_HOST + "/api/v1/users.json";

    public static String getLinkedInUrl() {
        return LINKEDIN_URL;
    }

    public static String getApplicants(String uid) {
        return API_HOST + "/api/v1/users/" + uid;
    }

    public static String getPropertySearchUrl() {
        return API_HOST + "/api/v2/rental_offerings.json";
    }

    public static String loadRentalListData() {
        return API_HOST + "/api/v2/rental_offerings?mapping_data=false";
    }

    public static String loadMapPinData() {
        return API_HOST + "/api/v2/rental_offerings?mapping_data=true";
    }

    public static String getPropertySearchUrl(String parameters) {
        return API_HOST + "/api/v1/rental_offerings.json?" + parameters;
    }

    public static String getFavoritesUrl() {
        return API_HOST + "/api/v2/rental_offerings?starred=true";
    }

    public static String getApplyUrl() {
        return API_HOST + "/api/v1/applications";
    }

    public static String propertyManagersUrl() {
        return API_HOST + "/api/v1/property_managers";
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
        return API_HOST + "/api/v1/cosigner_invites";
    }

    public static String sentCosignerInvites() {
        return cosignerInvitesUrl() + "?as_inviter=true";
    }

    public static String deleteCosignerInvite(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId;
    }

    public static String propertyPhotos(String rentalOfferingId) {
        return API_HOST + "/api/v1/property_photos?rental_offering_id=" + rentalOfferingId;
    }

    public static String cosignerProfilesUrl(String cosignerProfileId) {
        return API_HOST + "/api/v1/cosigner_profiles/" + cosignerProfileId;
    }

    public static String specificUserUrl(String userId) {
        return API_HOST + "/api/v1/users/" + userId;
    }

    public static String deleteProfile(String userId) {
        return API_HOST + "/api/v1/profiles/" + userId;
    }

    public static String signLeaseUrl(int leaseId) {
        return API_HOST + "/api/v1/leases/" + leaseId + "/embedded_signature_url";
    }

    public static String getAcceptCosignerInviteUrl(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId + "/accept";
    }

    public static String getDenyCosignerInviteUrl(int inviteId) {
        return cosignerInvitesUrl() + "/" + inviteId + "/deny";
    }

    public static String getRental(String rental_id) {
        return API_HOST + "/api/v1/rental_offerings/" + rental_id;
    }

    public static String deleteRentalStar(String star_id) {
        return API_HOST + "/api/v1/starred_properties/" + star_id;
    }

    public static String postRentalStar() {
        return API_HOST + "/api/v1/starred_properties";
    }

    public static String getAddProvider(String providerId) {
        return API_HOST + "/api/v1/sessions/add_providers";
    }

    public static String getApplicantPassword() {
        return API_HOST + "/api/v1/users/password";
    }

    public static String getSignin() {
        return API_HOST + "/api/v1/users/sign_in.json";
    }

    public static String getSignOut() {
        return API_HOST + "/api/v1/users/sign_out.json";
    }

    public static String getTransactions() {
        return API_HOST + "/api/v1/transactions";
    }

    public static String getPayments() {
        return API_HOST + "/api/v1/payments";
    }

    public static String getProfile(String id) {
        String url = API_HOST + "/api/v1/profiles";
        if (TextUtils.isEmpty(id)) return url;
        return url + "/" + id;
    }

    public static String getRoommateGroups(String id) {
        String url = API_HOST + "/api/v1/roommate_groups";
        if (TextUtils.isEmpty(id)) return url;
        return url + "/" + id;
    }

    public static String getRoommateGroupRemoveUser(String groupId, String userId) {
        String url = API_HOST + "/api/v1/roommate_groups";
        if (TextUtils.isEmpty(groupId) || TextUtils.isEmpty(userId)) return null;
        return url + "/" + groupId + "/remove_user?user_id=" + userId;
    }

    public static String getRoommateInvites(String roommate_group_id) {
        String url = API_HOST + "/api/v1/roommate_invites";
        if (TextUtils.isEmpty(roommate_group_id)) return url;
        return url + "/" + roommate_group_id;
    }

    public static String getRoommateInviteAccept(String inviteId) {
        if (TextUtils.isEmpty(inviteId)) return null;
        String url = API_HOST + "/api/v1/roommate_invites";
        return url + "/" + inviteId + "/accept";
    }

    public static String getRoommateInviteDeny(String inviteId) {
        if (TextUtils.isEmpty(inviteId)) return null;
        String url = API_HOST + "/api/v1/roommate_invites";
        return url + "/" + inviteId + "/deny";
    }

    public static String getLease(String leaseId) {
        String url = API_HOST + "/api/v1/leases";
        if (TextUtils.isEmpty(leaseId)) return null;
        return url + "/" + leaseId;
    }

    public static String getLeaseRoommatePayments(String leaseId) {
        String url = API_HOST + "/api/v1/leases";
        if (TextUtils.isEmpty(leaseId)) return null;
        return url + "/" + leaseId + "/roommate_payments";
    }

    public static String postApplication() {
        return API_HOST + "/api/v1/applications";
    }

    public static String getFullAddress(String place_id) {
        return GOOGLE_MAPS + "?placeid=" + place_id + "&sensor=false&key=" + GOOGLE_MAPS_API_KEY;
    }

}
