package com.rentalgeek.android.utils;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajohns on 9/29/15.
 *
 */
public class ResponseParser {

    public ErrorMsg humanizedErrorMsg(String response) {
        String key = "";
        String msg = "";
        try {
            JSONObject outerObject = new JSONObject(response);
            JSONObject innerError = outerObject.getJSONObject("errors");
            key = innerError.keys().next();

            JSONArray errorArray = innerError.getJSONArray(key);
            msg = errorArray.getString(0).toString();

            key = humanize(key);
            msg = humanize(msg);

            return new ErrorMsg(key, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ErrorMsg("", "");
    }


    public static String humanize(String str) {
        str = str.replace("_", " ");
        return WordUtils.capitalize(str);
    }

    public class ErrorMsg {
        public String title;
        public String msg;

        public ErrorMsg(String title, String msg) {
            this.title = title;
            this.msg = msg;
        }
    }

}
