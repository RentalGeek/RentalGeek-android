package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

public class MapRental {

    // TODO: DROP THE MAPRENTALS WRAPPER CLASS AND MAKE CONTAINER OBJECT

    @SerializedName("id") public Integer id;
    @SerializedName("latitude") public Double latitude;
    @SerializedName("longitude") public Double longitude;
    @SerializedName("bedroom_count") public Integer bedroomCount;

}
