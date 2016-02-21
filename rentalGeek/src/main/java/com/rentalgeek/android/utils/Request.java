package com.rentalgeek.android.utils;

public class Request {

    public static String serialize(String param) {
        param = param.toLowerCase();
        param = param.replace(" ", "_");
        return param;
    }

}
