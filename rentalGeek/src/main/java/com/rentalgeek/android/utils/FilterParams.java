package com.rentalgeek.android.utils;

import java.util.HashMap;
import java.util.Map;

public enum FilterParams {

    // TODO: (NOW) SET INITIAL PARAMS FOR LAT/LNG/RADIUS ON APP START AND MAKE SURE THEY ARE GOTTEN FROM MAP PARAMETERS AND MAP WILL RE-SET TO OLD LOCATION AND RADIUS ON NEXT APP START
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
