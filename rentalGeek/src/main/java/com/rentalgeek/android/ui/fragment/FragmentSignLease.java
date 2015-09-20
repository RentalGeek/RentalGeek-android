package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentSignLease  extends GeekBaseFragment {

    public static final String STREET = "streetAddress";
    public static final String CITY_STATE_ZIP = "cityStateZipAddress";

    @InjectView(R.id.street_address) TextView streetAddressTextView;
    @InjectView(R.id.city_state_zip_address) TextView cityStateZipAddressTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_lease, container, false);
        ButterKnife.inject(this, view);

        String street = getArguments().getString(STREET);
        String cityStateZip = getArguments().getString(CITY_STATE_ZIP);

        streetAddressTextView.setText(street);
        cityStateZipAddressTextView.setText(cityStateZip);

        return view;
    }

}
