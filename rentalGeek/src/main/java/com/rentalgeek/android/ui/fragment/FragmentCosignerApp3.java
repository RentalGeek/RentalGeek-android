package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp4;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class FragmentCosignerApp3 extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_app3, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.next_button)
    public void nextButtonTapped() {
        if (validInput()) {
            // save to cosigner app singleton
            getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp4.class));
        }
    }

    private boolean validInput() {
        return true;
    }

}
