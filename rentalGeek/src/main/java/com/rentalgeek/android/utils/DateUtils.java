package com.rentalgeek.android.utils;

/**
 * Created by rajohns on 9/23/15.
 */
public class DateUtils {

    public static String monthNumberFromName(String monthName) {
        if (monthName.toLowerCase().equals("january")) {
            return "01";
        }

        if (monthName.toLowerCase().equals("february")) {
            return "02";
        }

        if (monthName.toLowerCase().equals("march")) {
            return "03";
        }

        if (monthName.toLowerCase().equals("april")) {
            return "04";
        }

        if (monthName.toLowerCase().equals("may")) {
            return "05";
        }

        if (monthName.toLowerCase().equals("june")) {
            return "06";
        }

        if (monthName.toLowerCase().equals("july")) {
            return "07";
        }

        if (monthName.toLowerCase().equals("august")) {
            return "08";
        }

        if (monthName.toLowerCase().equals("september")) {
            return "09";
        }

        if (monthName.toLowerCase().equals("october")) {
            return "10";
        }

        if (monthName.toLowerCase().equals("november")) {
            return "11";
        }

        if (monthName.toLowerCase().equals("december")) {
            return "12";
        }

        return "";
    }

}
