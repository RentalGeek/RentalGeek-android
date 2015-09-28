package com.rentalgeek.android.ui.fragment;

import com.rentalgeek.android.R;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

import android.os.Bundle;

import com.rentalgeek.android.utils.Constants;

import butterknife.OnClick;
import butterknife.InjectView;
import butterknife.ButterKnife;

import com.rentalgeek.android.RentalGeekApplication;

public class FragmentProfileForm1 extends GeekBaseFragment {
    
    @InjectView(R.id.dob_month) Spinner months_spinner;
    @InjectView(R.id.dob_day) Spinner days_spinner;
    @InjectView(R.id.dob_year) Spinner years_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_profile_form1,container,false);
        ButterKnife.inject(this,view);
        
        String[] months = RentalGeekApplication.getStringArray(R.array.months);
        String[] days = RentalGeekApplication.getStringArray(R.array.days);
        String[] years = Constants.getYears();

        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, R.id.item_text,months);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, R.id.item_text, days);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, R.id.item_text, years);

        months_spinner.setAdapter(monthsAdapter);
        days_spinner.setAdapter(daysAdapter);
        years_spinner.setAdapter(yearsAdapter);
        
        return view;
    }
}
