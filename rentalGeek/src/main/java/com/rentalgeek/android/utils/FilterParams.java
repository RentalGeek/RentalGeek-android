package com.rentalgeek.android.utils;

import java.util.HashMap;
import java.util.Map;

public enum FilterParams {

    // TODO: (NOW) GET PROPERTY_MANAGER_ID WORKING FOR PARAMS AND MAKE SURE PROPERLY RELOADING MAP AND LIST SCREENS AND MAKE SURE IT IS SAVED AND RE-SET FROM APPPREFS ON NEW LOADING OF APP
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
