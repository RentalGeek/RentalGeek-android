package com.luttu.fragmentutils;

import org.json.JSONObject;


public interface VolleyOnResponseListener {

	public void onVolleyResponse(JSONObject result, int code);

	public void onVolleyError(String result, int code);
}
