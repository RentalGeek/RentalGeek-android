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
 * @purpose Third slide introduction
 *
 */
public class FragmentTutorialGeekLink extends Fragment {
  
	@InjectView(R.id.armed)
	TextView armed;

	public static FragmentTutorialGeekLink newInstance() {
		FragmentTutorialGeekLink fragment = new FragmentTutorialGeekLink();

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_tutorial_geeklink, container, false);
		
		ButterKnife.inject(this,v);
		armed.setText(Html.fromHtml(getActivity().getResources().getString(R.string.armed)));

		return v;

	}
	@Override
	public void onDestroyView() {

		super.onDestroyView();
		ButterKnife.reset(this);
	}

}
