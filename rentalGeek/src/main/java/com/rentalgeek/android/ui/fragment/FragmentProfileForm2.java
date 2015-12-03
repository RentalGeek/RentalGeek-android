package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;

public class FragmentProfileForm2 extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        //TODO Make fragment profile form 2 layout
        View view = inflater.inflate(R.layout.fragment_profile_form1, container, false);
        ButterKnife.inject(this, view);

        return view;
    }
}
