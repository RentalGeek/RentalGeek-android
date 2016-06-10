package com.rentalgeek.android.pojos;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RentalDetail {

    public static class Container {
        public RentalDetail rental_offering;
    }

    public static RentalDetail fromJson(String json) {
        Gson gson = new Gson();
        RentalDetail.Container container = gson.fromJson(json, RentalDetail.Container.class);
        return container.rental_offering;
    }

    @SerializedName("id") public Integer id;
    @SerializedName("bedroom_count") public Integer bedroomCount;
    @SerializedName("full_bathroom_count") public Integer bathroomCount;
    @SerializedName("rent") public Integer rent;
    @SerializedName("square_feet") public Integer squareFeet;
    @SerializedName("customer_contact_email_address") public String contactEmail;
    @SerializedName("customer_contact_phone_number") public String contactNumber;
    @SerializedName("salesy_description") public String description;
    @SerializedName("scrape_amenities") public ArrayList<String> amenities;
    @SerializedName("starred") public Boolean starred;
    @SerializedName("starred_property_id") public Integer starredPropertyId;
    @SerializedName("primary_property_photo_url") public String primaryPhotoUrl;
    @SerializedName("address") public String address;
    @SerializedName("city") public String city;
    @SerializedName("state") public String state;
    @SerializedName("zipcode") public String zipcode;
    @SerializedName("property_manager_id") public Integer propertyManagerId;
    @SerializedName("already_applied") public Boolean alreadyApplied;
    @SerializedName("rental_complex_name") public String complexName;
    @SerializedName("has_photos") public Boolean hasPhotos;
    @SerializedName("latitude") public Double latitude;
    @SerializedName("longitude") public Double longitude;

}
