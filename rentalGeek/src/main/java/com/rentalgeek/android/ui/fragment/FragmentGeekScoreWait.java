package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;


public class FragmentGeekScoreWait extends GeekBaseFragment {

	private static final String TAG = "FragmentGeekScoreWait";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View view = inflater.inflate(R.layout.fragment_geekscore_wait,viewGroup,false);
		return view;
	}
}
