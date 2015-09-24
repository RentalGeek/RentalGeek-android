package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.YesNoAnswer;
import com.rentalgeek.android.utils.OkAlert;
import com.rentalgeek.android.utils.YesNoCheckChangedListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class FragmentCosignerApp4 extends GeekBaseFragment {

    @InjectView(R.id.employer_edittext) EditText employerEditText;
    @InjectView(R.id.position_edittext) EditText positionEditText;
    @InjectView(R.id.income_edittext) EditText incomeEditText;
    @InjectView(R.id.cover_rent_segment) SegmentedGroup coverRentSegment;

    private String employer;
    private String position;
    private String income;
    private YesNoAnswer coverRent = new YesNoAnswer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_app4, container, false);
        ButterKnife.inject(this, view);

        coverRentSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        coverRentSegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(coverRent));

        return view;
    }

    @OnClick(R.id.submit_button)
    public void nextButtonTapped() {
        if (validInput()) {
            // make api call
        }
    }

    private boolean validInput() {
        employer = employerEditText.getText().toString().trim();
        position = positionEditText.getText().toString().trim();
        income = incomeEditText.getText().toString().trim();

        if (employer.equals("")) {
            OkAlert.show(getActivity(), "Employer", "Please enter your employer name.");
            return false;
        }

        if (position.equals("")) {
            OkAlert.show(getActivity(), "Employment Position", "Please enter your employment position");
            return false;
        }

        if (income.equals("")) {
            OkAlert.show(getActivity(), "Monthly Income", "Please enter your monthly income.");
            return false;
        }

        return true;
    }

}
