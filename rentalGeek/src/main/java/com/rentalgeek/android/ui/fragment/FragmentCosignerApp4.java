package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class FragmentCosignerApp4 extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_app4, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.submit_button)
    public void nextButtonTapped() {
        if (validInput()) {
            // save to cosigner app singleton
            // make api call to submit cosigner application
        }
    }

    private boolean validInput() {
        return true;
    }

}
