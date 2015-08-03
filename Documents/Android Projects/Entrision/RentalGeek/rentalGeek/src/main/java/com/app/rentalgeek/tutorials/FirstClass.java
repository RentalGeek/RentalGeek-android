package com.app.rentalgeek.tutorials;


import com.app.rentalgeek.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 
 * @author George
 * 
 * @purpose First slide introduction
 *
 */
public class FirstClass extends Fragment{
	
	public static FirstClass newInstance() {
		FirstClass fragment = new FirstClass();


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
