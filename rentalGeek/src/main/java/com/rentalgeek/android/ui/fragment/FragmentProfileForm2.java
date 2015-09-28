package com.rentalgeek.android.ui.fragment;

import com.rentalgeek.android.R;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

import android.os.Bundle;

import butterknife.OnClick;
import butterknife.InjectView;
import butterknife.ButterKnife;

import com.rentalgeek.android.RentalGeekApplication;

public class FragmentProfileForm2 extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle bundle) {
        
        //TODO Make fragment profile form 2 layout
        View view = inflater.inflate(R.layout.fragment_profile_form1,container,false);
        ButterKnife.inject(this,view);
        
        return view;
    }
}
