package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp2;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.SimpleSpinner;
import com.rentalgeek.android.utils.DateUtils;
import com.rentalgeek.android.utils.OkAlert;
import com.rentalgeek.android.utils.ResponseParser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentCosignerApp1 extends GeekBaseFragment {

    @InjectView(R.id.first_name_edittext) EditText firstNameEditText;
    @InjectView(R.id.last_name_edittext) EditText lastNameEditText;
    @InjectView(R.id.month_spinner) SimpleSpinner monthSpinner;
    @InjectView(R.id.day_spinner) SimpleSpinner daySpinner;
    @InjectView(R.id.year_edittext) EditText yearEditText;
    @InjectView(R.id.ssn_edittext) EditText ssnEditText;
    @InjectView(R.id.marital_status_spinner) SimpleSpinner maritalStatusSpinner;
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

        AppPreferences.putCosignerProfilePosition(1);
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
            RequestParams params = new RequestParams();
            params.put("user[cosigner_profile_attributes][step]", "personal_info");
            params.put("user[first_name]", firstName);
            params.put("user[last_name]", lastName);
            params.put("user[cosigner_profile_attributes][marital_status]", maritalStatus);
            params.put("user[cosigner_profile_attributes][born_on]", birthYear + "-" + DateUtils.monthNumberFromName(birthMonth) + "-" + birthDay);
            params.put("user[cosigner_profile_attributes][ssn]", ssn);
            params.put("user[cosigner_profile_attributes][phone]", phoneNumber);

            GlobalFunctions.putApiCall(getActivity(), ApiManager.specificUserUrl(SessionManager.Instance.getCurrentUser().id), params, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    showProgressDialog(R.string.dialog_msg_loading);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideProgressDialog();
                }

                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    LoginBackend detail = (new Gson()).fromJson(content, LoginBackend.class);
                    SessionManager.Instance.onUserLoggedIn(detail);
                    getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp2.class));
                }

                @Override
                public void onFailure(Throwable ex, String failureResponse) {
                    super.onFailure(ex, failureResponse);
                    ResponseParser.ErrorMsg errorMsg = new ResponseParser().humanizedErrorMsg(failureResponse);
                    OkAlert.show(getActivity(), errorMsg.title, errorMsg.msg);
                }
            });
        }
    }

    private boolean validInput() {
        firstName = firstNameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        birthMonth = monthSpinner.getSelectedItem().toString();
        birthDay = daySpinner.getSelectedItem().toString();
        birthYear = yearEditText.getText().toString().trim();
        ssn = ssnEditText.getText().toString().trim();
        maritalStatus = maritalStatusSpinner.getSelectedItem().toString().toLowerCase();
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
        monthSpinner.populate(getActivity(), R.array.months_array);
        daySpinner.populate(getActivity(), R.array.days_array);
        maritalStatusSpinner.populate(getActivity(), R.array.marital_status_array);
    }

}
