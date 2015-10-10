package com.rentalgeek.android.net;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rentalgeek.android.api.SessionManager;

import org.apache.http.client.HttpResponseException;

import java.net.HttpURLConnection;

public class GeekHttpResponseHandler extends AsyncHttpResponseHandler {
 
    private static final String TAG = GeekHttpResponseHandler.class.getSimpleName();

    public void onAuthenticationFailed() {
        SessionManager.Instance.onUserLoggedOut();
    }

    //public void onBeforeStart() { }

    //public void onFinish() { }

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
