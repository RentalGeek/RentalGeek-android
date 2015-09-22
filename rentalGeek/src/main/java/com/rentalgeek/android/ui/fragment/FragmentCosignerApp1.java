package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.CosignerApplication;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp2;
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
    @InjectView(R.id.ssn_edittext) EditText ssnEditText;
    @InjectView(R.id.marital_status_spinner) Spinner maritalStatusSpinner;
    @InjectView(R.id.phone_number_edittext) EditText phoneNumberEditText;

    private String firstName;
    private String lastName;
    private String birthMonth;
    private String birthDay;
    private String birthYear;
    private String ssn;
    private String maritalStatus;
    private String phoneNumber;

    @Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_app1, container, false);
		ButterKnife.inject(this, view);

        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ssnEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if user is typing string one character at a time
                if (count == 1) {
                    // auto insert dashes while user typing their ssn
                    if (start == 2 || start == 5) {
                        ssnEditText.setText(ssnEditText.getText() + "-");
                        ssnEditText.setSelection(ssnEditText.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

		setUpSpinners();
		return view;
	}

	@OnClick(R.id.next_button)
	public void nextButtonTapped() {
		if (validInput()) {
            saveFormValuesToCosignerApplication();
            getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp2.class));
        }
	}

	private boolean validInput() {
        firstName = firstNameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        birthMonth = monthSpinner.getSelectedItem().toString();
        birthDay = daySpinner.getSelectedItem().toString();
        birthYear = yearEditText.getText().toString().trim();
        ssn = ssnEditText.getText().toString().trim();
        maritalStatus = maritalStatusSpinner.getSelectedItem().toString();
        phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (firstName.equals("")) {
            OkAlert.show(getActivity(), "First Name", "Please enter your first name.");
            return false;
        }

        if (lastName.equals("")) {
            OkAlert.show(getActivity(), "Last Name", "Please enter your last name.");
            return false;
        }

        if (birthMonth.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a month.");
            return false;
        }

        if (birthDay.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a day.");
            return false;
        }

        if (birthYear.equals("")) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a year.");
            return false;
        }

        if (birthYear.length() < 4) {
            OkAlert.show(getActivity(), "Date of Birth", "Please enter a valid year in format 'YYYY'.");
            return false;
        }

        ssn = ssn.replaceAll("[^0-9]", "");
        if (ssn.equals("")) {
            OkAlert.show(getActivity(), "SSN", "Please enter your SSN.");
            return false;
        }

        if (ssn.length() < 9) {
            OkAlert.show(getActivity(), "SSN", "Please enter a valid 9-digit SSN.");
            return false;
        }

        if (maritalStatus.equals("")) {
            OkAlert.show(getActivity(), "Marital Status", "Please enter your marital status.");
            return false;
        }

        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (phoneNumber.equals("")) {
            OkAlert.show(getActivity(), "Phone Number", "Please enter your phone number.");
            return false;
        }

        return true;
	}

	private void setUpSpinners() {
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.days_array, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.marital_status_array, android.R.layout.simple_spinner_item);
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatusSpinner.setAdapter(maritalStatusAdapter);
    }

    private void saveFormValuesToCosignerApplication() {
        CosignerApplication.INSTANCE.setFirstName(firstName);
        CosignerApplication.INSTANCE.setLastName(lastName);
        CosignerApplication.INSTANCE.setBirthMonth(birthMonth);
        CosignerApplication.INSTANCE.setBirthDay(birthDay);
        CosignerApplication.INSTANCE.setBirthYear(birthYear);
        CosignerApplication.INSTANCE.setSSN(ssn);
        CosignerApplication.INSTANCE.setMaritalStatus(maritalStatus);
        CosignerApplication.INSTANCE.setPhoneNumber(phoneNumber);
    }

}
