package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

public class ListRental {

    // TODO (MINOR): DROP THE LISTRENTALS WRAPPER CLASS AND MAKE CONTAINER OBJECT

    @SerializedName("id") public Integer id;
    @SerializedName("rent") public Integer rent;
    @SerializedName("address") public String address;
    @SerializedName("city") public String city;
    @SerializedName("state") public String state;
    @SerializedName("zipcode") public String zipcode;
    @SerializedName("bedroom_count") public Integer bedroomCount;
    @SerializedName("full_bathroom_count") public Integer bathroomCount;
    @SerializedName("starred_property_id") public Integer starredPropertyId;
    @SerializedName("primary_property_photo_url") public String primaryPhotoUrl;

}
