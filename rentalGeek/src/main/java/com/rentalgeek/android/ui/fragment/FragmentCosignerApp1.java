package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.rentalgeek.android.R;
import com.rentalgeek.android.utils.OkAlert;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentCosignerApp1 extends GeekBaseFragment {

	@InjectView(R.id.first_name_edittext) EditText firstNameEditText;
	@InjectView(R.id.last_name_edittext) EditText lastNameEditText;
	@InjectView(R.id.month_spinner) Spinner monthSpinner;
	@InjectView(R.id.day_spinner) Spinner daySpinner;
	@InjectView(R.id.year_edittext) EditText yearEditText;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_app, container, false);

		ButterKnife.inject(this, view);

		setUpBirthdaySpinners();
        
		return view;
	}

	@OnClick(R.id.next_button)
	public void nextButtonTapped() {
		if (validInput()) {
            // store all values in cosigner profile singleton
            // go to next cosigner app page
        }
	}

	private boolean validInput() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String birthdayMonth = monthSpinner.getSelectedItem().toString();
        String birthdayDay = daySpinner.getSelectedItem().toString();
        String birthdayYear = yearEditText.getText().toString().trim();

        if (firstName.equals("")) {
            OkAlert.show(getActivity(), "First Name", "Please enter your first name.");
            return false;
        }

        if (lastName.equals("")) {
            OkAlert.show(getActivity(), "Last Name", "Please enter your last name.");
            return false;
        }

        if (birthdayMonth.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a month.");
            return false;
        }

        if (birthdayDay.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a day.");
            return false;
        }

        if (birthdayYear.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a year.");
            return false;
        }

        if (birthdayYear.length() < 4) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a valid year in format 'YYYY'.");
            return false;
        }

        return true;
	}

	private void setUpBirthdaySpinners() {
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.days_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
    }

}
