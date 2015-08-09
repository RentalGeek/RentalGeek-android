package com.luttu.utils;


import org.apache.http.Header;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GlobalFunctions {

	public interface HttpResponseHandler {
		void handle(String response, boolean failre);
	}

	public static void postApiCall(final Context context, final String url,
			RequestParams params, AsyncHttpClient httpClient,
			final HttpResponseHandler handler) {

		httpClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {
				// TODO Auto-generated method stub
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

	public static void getApiCall(final Context context, final String url,
			AsyncHttpClient httpClient, final HttpResponseHandler handler) {
		// httpClient.get(context, url, headers, params, responseHandler);
		httpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {
				// TODO Auto-generated method stub

				super.onFailure(arg0, failureResponse);
				System.out.println("failureResponse==" + failureResponse
						+ "url ==" + url);
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

	public static void deleteApiCall(final Context context, final String url,
			AsyncHttpClient httpClient, final HttpResponseHandler handler) {

		httpClient.delete(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable error, String failureResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, error, failureResponse);

				handler.handle(failureResponse, false);
			}

			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				super.onSuccess(response);
				handler.handle(response, true);
			}

		});
	}

	// Put request

	public static void putApiCall(final Context context, final String url,
			RequestParams params, AsyncHttpClient httpClient,
			final HttpResponseHandler handler) {

		httpClient.put(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String failureResponse) {
				// TODO Auto-generated method stub
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
