package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;

public class FragmentCosignerApp extends GeekBaseFragment {

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_app, container, false);

		ButterKnife.inject(this,view);
        
		return view;
	}

}
