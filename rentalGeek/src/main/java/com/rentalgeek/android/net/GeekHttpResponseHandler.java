package com.rentalgeek.android.net;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.client.HttpResponseException;

import java.net.HttpURLConnection;


public class GeekHttpResponseHandler extends AsyncHttpResponseHandler {

    public void onAuthenticationFailed() { }

    public void onBeforeStart() { }

    public void onFinish() { }

    public void onSuccess(String content) { }


    @Override
    public void onFailure(Throwable ex, String failureResponse) {

        if (ex instanceof HttpResponseException) {
            HttpResponseException hre = (HttpResponseException) ex;
            int statusCode = hre.getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                onAuthenticationFailed();
            }
        }
    }
}
