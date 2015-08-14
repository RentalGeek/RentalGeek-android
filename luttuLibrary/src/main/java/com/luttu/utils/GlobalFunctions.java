package com.luttu.utils;


import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;

import java.net.HttpURLConnection;

public class GlobalFunctions {

	public interface HttpResponseHandler {
		void handle(String response, boolean failre);
	}

	public static void postApiCall(final Context context, final String url,
			RequestParams params, final String authToken, AsyncHttpClient httpClient,
			final HttpResponseHandler handler) {

        if (!TextUtils.isEmpty(authToken)) {
            httpClient.addHeader("Authorization", String.format("Token token=%s"));
        }

		httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(Throwable arg0, String failureResponse) {

                super.onFailure(arg0, failureResponse);
                System.out.println("fail" + failureResponse + "url is" + url);
                handler.handle(failureResponse, false);

                // errorToast(context);
            }

            @Override
            public void onSuccess(String response) {

                System.out.println("response" + response);
                handler.handle(response, true);

            }
        });
	}

	public static void getApiCall(final Context context, final String url, final String authToken, AsyncHttpClient httpClient, final HttpResponseHandler handler) {
		// httpClient.get(context, url, headers, params, responseHandler);

        if (!TextUtils.isEmpty(authToken)) {
            httpClient.addHeader("Authorization", String.format("Token token=%s"));
        }

		httpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {

                if (arg0 instanceof HttpResponseException) {
                    HttpResponseException hre = (HttpResponseException) arg0;
                    int statusCode = hre.getStatusCode();
                    if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {

                    }
                }
				super.onFailure(arg0, failureResponse);
				System.out.println("failureResponse==" + failureResponse + "url ==" + url);
				// errorToast(context);


				handler.handle(failureResponse, false);
			}

			@Override
			public void onSuccess(String response) {

				// handler.handle(response);
				handler.handle(response, true);
			}
		});

	}

	public static void deleteApiCall(final Context context, final String url, final String authToken,
			AsyncHttpClient httpClient, final HttpResponseHandler handler) {

        if (!TextUtils.isEmpty(authToken)) {
            httpClient.addHeader("Authorization", String.format("Token token=%s"));
        }

		httpClient.delete(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable error, String failureResponse) {

				super.onFailure(statusCode, headers, error, failureResponse);

				handler.handle(failureResponse, false);
			}

			@Override
			public void onSuccess(String response) {

				super.onSuccess(response);
				handler.handle(response, true);
			}

		});
	}

	// Put request

	public static void putApiCall(final Context context, final String url,
			RequestParams params, final String authToken, AsyncHttpClient httpClient,
			final HttpResponseHandler handler) {

        if (!TextUtils.isEmpty(authToken)) {
            httpClient.addHeader("Authorization", String.format("Token token=%s"));
        }

		httpClient.put(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {

				super.onFailure(arg0, failureResponse);
				System.out.println("fail" + failureResponse + "url is" + url);
				handler.handle(failureResponse, false);

				// errorToast(context);
			}

			@Override
			public void onSuccess(String response) {

				System.out.println("response" + response);
				handler.handle(response, true);

			}
		});

	}

}
