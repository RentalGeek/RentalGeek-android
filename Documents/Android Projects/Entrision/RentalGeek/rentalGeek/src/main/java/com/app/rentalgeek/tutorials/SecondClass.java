package com.app.rentalgeek.tutorials;

import com.app.rentalgeek.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author George
 * 
 * @purpose Second slide introduction
 *
 */
public class SecondClass extends Fragment {

 	
	
	@InjectView(R.id.four_one)
	TextView four;
	
	public static SecondClass newInstance() {
		SecondClass fragment = new SecondClass();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			View v=inflater.inflate(R.layout.second_class, container,false);
			ButterKnife.inject(this,v);
			
			four.setText(Html.fromHtml(getActivity().getResources().getString(R.string.fourthirty)));
			return v;
	
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
	}

}
