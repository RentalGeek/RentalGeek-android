package com.rentalgeek.android.ui.fragment;

import android.view.View;
import android.os.Bundle;
import butterknife.InjectView;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

public class FragmentCosignApp extends Fragment {

	private static final String TAG = "FragmentCosignApp";

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosign_app,container,false);

		ButterKnife.inject(this,view);
        
		return view;
	}
}
