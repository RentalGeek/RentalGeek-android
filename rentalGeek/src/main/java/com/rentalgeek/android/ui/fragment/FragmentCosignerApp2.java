package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.CosignerApplication;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp3;
import com.rentalgeek.android.utils.OkAlert;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class FragmentCosignerApp2 extends GeekBaseFragment {

    @InjectView(R.id.address_edittext) EditText addressEditText;
    @InjectView(R.id.city_edittext) EditText cityEditText;
    @InjectView(R.id.state_spinner) Spinner stateSpinner;
    @InjectView(R.id.zip_edittext) EditText zipEditText;
    @InjectView(R.id.own_or_rent_segment) SegmentedGroup ownOrRentSegment;
    @InjectView(R.id.monthly_payment_edittext) EditText monthlyPaymentEditText;

    private String address;
    private String city;
    private String state;
    private String zip;
    private String ownsOrRents;
    private String monthlyPayment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_app2, container, false);
        ButterKnife.inject(this, view);

        ownOrRentSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        ownOrRentSegment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.owns_button:
                        ownsOrRents = "owns";
                        break;
                    case R.id.rents_button:
                        ownsOrRents = "rents";
                        break;
                    default:
                        break;
                }
            }
        });

        setUpSpinners();

        return view;
    }

    @OnClick(R.id.next_button)
    public void nextButtonTapped() {
        if (validInput()) {
            saveFormValuesToCosignerApplication();
            getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp3.class));
        }
    }

    private boolean validInput() {
        address = addressEditText.getText().toString().trim();
        city = cityEditText.getText().toString().trim();
        state = stateSpinner.getSelectedItem().toString();
        zip = zipEditText.getText().toString().trim();
        monthlyPayment = monthlyPaymentEditText.getText().toString().trim();

        if (address.equals("")) {
            OkAlert.show(getActivity(), "Address", "Please enter your address.");
            return false;
        }

        if (city.equals("")) {
            OkAlert.show(getActivity(), "City", "Please enter your city.");
            return false;
        }

        if (state.equals("")) {
            OkAlert.show(getActivity(), "State", "Please enter your state.");
            return false;
        }

        if (zip.equals("")) {
            OkAlert.show(getActivity(), "Zip Code", "Please enter your zip code.");
            return false;
        }

        if (zip.length() < 5) {
            OkAlert.show(getActivity(), "Zip Code", "Please enter a valid 5-digit zip code.");
            return false;
        }

        if (ownsOrRents == null) {
            OkAlert.show(getActivity(), "Own or Rent", "Please select whether you currently own or rent a home.");
            return false;
        }

        if (monthlyPayment.equals("")) {
            OkAlert.show(getActivity(), "Monthly Payment", "Please enter you current monthly payment.");
            return false;
        }

        return true;
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.state_array, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
    }

    private void saveFormValuesToCosignerApplication() {
        CosignerApplication.INSTANCE.setAddress(address);
        CosignerApplication.INSTANCE.setCity(city);
        CosignerApplication.INSTANCE.setState(state);
        CosignerApplication.INSTANCE.setZip(zip);
        CosignerApplication.INSTANCE.setOwnsOrRents(ownsOrRents);
        CosignerApplication.INSTANCE.setMonthlyPayment(monthlyPayment);
    }

}
