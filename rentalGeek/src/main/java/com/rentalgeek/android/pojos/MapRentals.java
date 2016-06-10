package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MapRentals {

    @SerializedName("rental_offerings")
    public ArrayList<MapRental> all;

}
