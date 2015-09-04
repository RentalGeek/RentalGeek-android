package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;


public class FragmentRoommates  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommates, container, false);

        ButterKnife.inject(this, v);

        return v;

    }
}
