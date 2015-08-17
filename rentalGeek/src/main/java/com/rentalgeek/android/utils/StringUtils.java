package com.rentalgeek.android.utils;

public class StringUtils  {


    public static boolean isNotNullAndEquals(String value, String equalTo) {
        if (value == null) return false;
        return value.equals(equalTo);
    }
}
