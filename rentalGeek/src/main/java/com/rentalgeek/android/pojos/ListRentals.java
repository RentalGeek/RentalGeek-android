package com.rentalgeek.android.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListRentals {

    @SerializedName("rental_offerings")
    public ArrayList<ListRental> all;

}
