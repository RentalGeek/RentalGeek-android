package com.luttu.fragmentutils;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.VolleyForAll.HttpRequestType;
import com.luttu.gson.DetailFalse;
import com.luttu.utils.GetSSl;
import com.luttu.utils.GlobalFunctions;
import com.luttu.utils.GlobalFunctions.HttpResponseHandler;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/****
 * extend LuttuBaseAbstract for network operations.
 * */
public abstract class LuttuBaseAbstract extends LuttuBaseFragment {
	/****
	 * Receive parse response here
	 * */
	public abstract void parseresult(String response, boolean success, int value);

	AsyncHttpClient client;
	PersistentCookieStore mCookieStore;

	/****
	 * Receive parse error response here
	 * */
	public abstract void error(String response, int value);

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		client = new AsyncHttpClient();
		try {
			LuttuBaseFragmentActivity mainActivity = (LuttuBaseFragmentActivity) getActivity();
			// mainActivity.backButtonClickedListener = this;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/****
	 * AsyncHttpClient network operation
	 * */
	public void asynkhttp(RequestParams params, final int value, String link, final String authToken,
			boolean progrss) {

		if (progrss)
			progressshow();
		new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(getActivity());
		client.setCookieStore(mCookieStore);

		GlobalFunctions.postApiCall(getActivity(), link, params, authToken, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {

						try {
							progresscancel();
							List<Cookie> cookies = mCookieStore.getCookies();
							System.out.println("cockeui    " + cookies.size());
							for (Cookie c : cookies) {
								if (c.getName().equals("_fonda_session")) {
									System.out.println("_fonda_session"
											+ c.getValue());
								}
							}

							if (success && getActivity() != null) {
								afterparse(response, value);
							} else if (getActivity() != null) {
								error(response, value);
								// toast("Connection error" + response);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}
				});
	}

	/***
	 * 
	 * AsyncHttpClient network operation
	 */

	public void asynkhttpGet(final int value, String link, final String authToken, boolean progrss) {

		if (progrss) progressshow();

        System.out.println("get:"+link);

		AsyncHttpClient client = new AsyncHttpClient();
		new GetSSl().getssl(client);
		PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());
		client.setCookieStore(myCookieStore);
		BasicClientCookie newCookie = new BasicClientCookie("cookiesare", "awesome");
		newCookie.setVersion(1);
		newCookie.setDomain("dev.rentalgeek.com");
		newCookie.setPath("/");
		myCookieStore.addCookie(newCookie);

		GlobalFunctions.getApiCall(getActivity(), link, authToken, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean failre) {

						progresscancel();
						if (failre && getActivity() != null) {
							afterparse(response, value);
						} else if (getActivity() != null) {
							error(response, value);
							// toast("Connection error" + response);
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}

				});

	}

	/*****
	 * AsyncHttp Delete method
	 */
	public void asynkhttpDelete(final int value, String link, final String authToken, boolean progrss) {
	
		if (progrss)
			progressshow();
		new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(getActivity());
		client.setCookieStore(mCookieStore);
		GlobalFunctions.deleteApiCall(getActivity(), link, authToken, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean failre) {

						progresscancel();
						if (failre && getActivity() != null) {
							afterparse(response, value);
						} else if (getActivity() != null) {
							error(response, value);
							// toast("Connection error" + response);
						}

					}

					@Override
					public void onAuthenticationFailed() {

					}
				});
	}
	
	
	/****
	 * AsyncHttpClient PUT operation
	 * */
	public void asynkhttpPut(RequestParams params, final int value, String link, final String authToken,
			boolean progrss) {

		if (progrss)
			progressshow();
		new GetSSl().getssl(client);
		mCookieStore = new PersistentCookieStore(getActivity());
		client.setCookieStore(mCookieStore);

		GlobalFunctions.putApiCall(getActivity(), link, params, authToken, client,
				new HttpResponseHandler() {

					@Override
					public void handle(String response, boolean success) {

						progresscancel();
						List<Cookie> cookies = mCookieStore.getCookies();
						System.out.println("cockeui    " + cookies.size());
						for (Cookie c : cookies) {
							if (c.getName().equals("_fonda_session")) {
								System.out.println("_fonda_session"
										+ c.getValue());
							}
						}

						if (success && getActivity() != null) {
							afterparse(response, value);
						} else if (getActivity() != null) {
							error(response, value);
							// toast("Connection error" + response);
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}
				});
	}

	/****
	 * Volley network operation
	 * */
	public void volley(HashMap<String, String> paramsMapAPI2, final int value,
			String link, boolean progrss, HttpRequestType type) {

		if (progrss)
			progressshow();
		VolleyForAll volley = new VolleyForAll(getActivity(),
				new VolleyOnResponseListener() {

					@Override
					public void onVolleyResponse(JSONObject result, int code) {

						progresscancel();
						if (getActivity() != null) {
							// System.out.println(result.toString());
							afterparse(result.toString(), value);
						}
					}

					@Override
					public void onVolleyError(String result, int code) {

						progresscancel();
						if (getActivity() != null) {
							error(result, value);
							toast("Connection error");
						}
					}
				});
		volley.volleyToNetwork(link, type, paramsMapAPI2, value);
	}

	private void afterparse(String response, int value) {

		try {
			DetailFalse session = (new Gson()).fromJson(response.toString(),
					DetailFalse.class);
			parseresult(response, true, value);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/****
	 * OkHttp network operation
	 * */
	public void okhttp(final String url, final int value, boolean progrss) {

		if (progrss)
			progressshow();
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder().url(url).build();

				try {
					Response response = client.newCall(request).execute();
					return response.body().string();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {

				super.onPostExecute(result);
				// System.out.println("result" + result);
				progresscancel();
				afterparse(result, value);
				// progressclose();
			}
		}.execute();
	}

	/****
	 * Receive location address
	 * */
	protected void locationname(Location location) {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ location.getLatitude() + "," + location.getLongitude()
				+ "&sensor=true";
		okhttp(url, 2, true);
	}
}
