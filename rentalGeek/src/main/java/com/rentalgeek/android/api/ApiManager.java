package com.rentalgeek.android.api;


public class ApiManager {

    // Default host
    public static String API_HOST = "http://104.236.100.21";

    public static String regis_link = "https://api.rentalgeek.com/rgapi/applicants.json";


    public static String getPropertySearchUrl(String location) {
        String url = API_HOST + "/v1/rental_offerings.json?" + location;
        return url;
    }

    public static String getStarredPrpoerties() {
        String url = API_HOST + "/v2/starred_properties";
        return url;
    }
}
