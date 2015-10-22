package com.rentalgeek.android.utils;

/**
 * Created by rajohns on 10/22/15.
 */
public class Request {

    public static String serialize(String param) {
        param = param.toLowerCase();
        param = param.replace(" ", "_");
        return param;
    }

}
