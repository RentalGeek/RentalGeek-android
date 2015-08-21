package com.rentalgeek.android.utils;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class StringUtils  {

    public static boolean isNotNullAndEquals(String value, String equalTo) {
        if (value == null) return false;
        return value.equals(equalTo);
    }

    public static boolean isTrimEmpty(EditText editText) {
        if (editText == null) return true;
        return editText.getText().toString().trim().equals("");
    }

    public static boolean isTrimEmpty(String value) {
        return TextUtils.isEmpty(getTrim(value));
    }

    public static String getTrim(String value) {
        if (value == null) return "";
        return value.trim();
    }

    public static String getTrimText(EditText editText) {
        if (editText == null) return "";
        return editText.getText().toString().trim();
    }

    public static String getTrimText(TextView editText) {
        if (editText == null) return "";
        return editText.getText().toString().trim();
    }
}
