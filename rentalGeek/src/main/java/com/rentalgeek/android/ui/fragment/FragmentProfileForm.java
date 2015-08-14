package com.rentalgeek.android.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;

public class FragmentProfileForm extends Fragment {

    private int position;

    public static FragmentProfileForm newInstance(int pos) {
        FragmentProfileForm fragment = new FragmentProfileForm();
        fragment.position = pos;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_geekscore_main, container, false);
        ButterKnife.inject(this, v);
//        con = new ConnectionDetector(getActivity());
//        appPref = new AppPrefes(getActivity(), "rentalgeek");
//        click_rent.setText(Html.fromHtml("<b>Click to Rent</b></font><sup>&#8482;</sup>"));
//
//        if (con.isConnectingToInternet()) {
//            CheckPaymentf();
//        } else {
//            toast("Please check you internet connection");
//        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
