package com.rentalgeek.android.tutorials;

import org.json.JSONObject;
import com.rentalgeek.android.R;
import com.luttu.fragmentutils.VolleyForAll;
import com.luttu.fragmentutils.VolleyOnResponseListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author George
 * 
 * @purpose Fourtht slide introduction
 *
 */
public class FourthClass extends Fragment implements VolleyOnResponseListener{
	
	VolleyForAll volley;
	
	 
	
	public static FourthClass newInstance() {
		FourthClass fragment = new FourthClass();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fourth_class, container, false);
		
		volley=new VolleyForAll(getActivity(), this);

		return v;

	}

	@Override
	public void onVolleyResponse(JSONObject result, int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVolleyError(String result, int code) {
		// TODO Auto-generated method stub
		
	}

}
