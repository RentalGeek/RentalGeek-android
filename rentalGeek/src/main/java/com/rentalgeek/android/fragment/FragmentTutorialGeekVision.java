package com.rentalgeek.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;


/**
 * 
 * @author George
 * 
 * @purpose First slide introduction
 *
 */
public class FragmentTutorialGeekVision extends Fragment{
	
	public static FragmentTutorialGeekVision newInstance() {
		FragmentTutorialGeekVision fragment = new FragmentTutorialGeekVision();


        return fragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.first_class, container,false);
		
		return v;
	}

}
