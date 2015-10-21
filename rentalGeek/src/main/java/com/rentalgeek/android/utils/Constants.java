package com.rentalgeek.android.utils;

import java.util.Calendar;

public class Constants {

    public static final String FULLNAME = "FULLNAME";
    public static final String PHOTO_URL = "PHOTO_URL";
    public static final String PROFILE_URL = "PROFILE_URL";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    private static String[] years;
    private static final int START_YEAR = 1900;

    public static String[] getYears() {
        if (years == null || years.length == 0) {

            int this_year = Calendar.getInstance().get(Calendar.YEAR);
            int range = this_year - START_YEAR;

            years = new String[range + 1];
            years[0] = "YYYY";

            for (int i = 1; i <= range; i++) {
                years[i] = Integer.toString(START_YEAR + i);
            }
        }

        return years;
    }


}
