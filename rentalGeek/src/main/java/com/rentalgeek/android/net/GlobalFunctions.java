package com.rentalgeek.android.net;


import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class GlobalFunctions {

    static AsyncHttpClient client;
    private static final int TIMEOUT = 20000;

    protected static AsyncHttpClient getClient() {
        if (client == null) {
            client = new AsyncHttpClient();
            client.setTimeout(TIMEOUT);
        }
        return client;
    }

    public static void postApiCall(final Context context, final String url, RequestParams params, final String authToken, final GeekHttpResponseHandler handler) {
        if (!TextUtils.isEmpty(authToken)) {
            getClient().addHeader("Authorization", String.format("Token token=%s", authToken));
        }

        getClient().post(url, params, handler);
    }

    public static void getApiCall(final Context context, final String url, final String authToken, final GeekHttpResponseHandler handler) {
        if (!TextUtils.isEmpty(authToken)) {
            getClient().addHeader("Authorization", String.format("Token token=%s", authToken));
        }

        getClient().get(url, handler);
    }

    public static void deleteApiCall(final Context context, final String url, final String authToken, final GeekHttpResponseHandler handler) {
        if (!TextUtils.isEmpty(authToken)) {
            getClient().addHeader("Authorization", String.format("Token token=%s", authToken));
        }

        getClient().delete(url, handler);
    }

    public static void putApiCall(final Context context, final String url, RequestParams params, final String authToken, final GeekHttpResponseHandler handler) {
        if (!TextUtils.isEmpty(authToken)) {
            getClient().addHeader("Authorization", String.format("Token token=%s", authToken));
        }

        getClient().put(url, params, handler);
    }

}
