package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

public class MapRental {

    public Integer id;
    public Double latitude;
    public Double longitude;

    @SerializedName("bedroom_count")
    public Integer bedroomCount;

}
