package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 
 * @author George
 * 
 * @purpose Second slide introduction
 *
 */
public class FragmentTutorialGeekScore extends Fragment {

 	
	
	@InjectView(R.id.four_one)
	TextView four;
	
	public static FragmentTutorialGeekScore newInstance() {
		FragmentTutorialGeekScore fragment = new FragmentTutorialGeekScore();
		
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			View v=inflater.inflate(R.layout.fragment_tutorial_geekscore, container,false);
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
