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
 * @purpose Third slide introduction
 *
 */
public class ThirdClass extends Fragment {
  
	@InjectView(R.id.armed)
	TextView armed;

	public static ThirdClass newInstance() {
		ThirdClass fragment = new ThirdClass();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.third_class, container, false);
		
		ButterKnife.inject(this,v);
		armed.setText(Html.fromHtml(getActivity().getResources().getString(R.string.armed)));

		return v;

	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
	}

}
