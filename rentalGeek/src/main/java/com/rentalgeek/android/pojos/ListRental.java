package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

public class ListRental {

    public Integer id;
    public Integer rent;
    public String address;
    public String city;
    public String state;
    public String zipcode;

    @SerializedName("bedroom_count")
    public Integer bedroomCount;

    @SerializedName("full_bathroom_count")
    public Integer bathroomCount;

    @SerializedName("starred_property_id")
    public Integer starredPropertyId;

    @SerializedName("primary_property_photo_url")
    public String primaryPhotoUrl;

}
