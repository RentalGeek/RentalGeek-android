package com.rentalgeek.android.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GeekGson {
    private static Gson gson;

    public static Gson getInstance() {
        
        if( gson == null ) {
            gson = new GsonBuilder().create();
        }
        
        return gson;
    }
}
