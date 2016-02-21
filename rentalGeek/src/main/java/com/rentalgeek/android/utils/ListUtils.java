package com.rentalgeek.android.utils;


import java.util.Collection;

public class ListUtils {

    public static <T> boolean isNullOrEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }

}
