package com.rentalgeek.android.utils;

import java.util.HashMap;
import java.util.Map;

public enum FilterParams {

    // TODO: GET ALL POSSIBLE PARAMS MENTIONED IN V2 REQUEST PARAMS WORKING

    INSTANCE;

    public Map<String, String> params = new HashMap<>();

    public String toString() {
        String paramString = "";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramString += "&" + entry.getKey() + "=" + entry.getValue();
        }

        return paramString;
    }

}
