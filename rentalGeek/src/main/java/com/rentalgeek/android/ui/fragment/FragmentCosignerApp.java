package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentCosignerApp extends GeekBaseFragment {

	@InjectView(R.id.first_name_edittext) EditText firstNameEditText;
	@InjectView(R.id.last_name_edittext) EditText lastNameEditText;
	@InjectView(R.id.month_spinner) Spinner monthSpinner;
	@InjectView(R.id.day_spinner) Spinner daySpinner;
	@InjectView(R.id.year_edittext) EditText yearEditText;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_app, container, false);

		ButterKnife.inject(this, view);
        
		return view;
	}

	@OnClick(R.id.next_button)
	public void nextButtonTapped() {
		verifyInput();
	}

	private void verifyInput() {

	}

}
