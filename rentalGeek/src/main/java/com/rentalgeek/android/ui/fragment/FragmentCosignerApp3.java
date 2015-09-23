package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.model.Answer;
import com.rentalgeek.android.model.CosignerApplication;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp4;
import com.rentalgeek.android.utils.YesNoCheckChangedListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public class FragmentCosignerApp3 extends GeekBaseFragment {

    @InjectView(R.id.lost_court_segment) SegmentedGroup lostCourtSegment;
    @InjectView(R.id.lawsuit_segment) SegmentedGroup lawsuitSegment;
    @InjectView(R.id.foreclosed_segment) SegmentedGroup foreclosedSegment;
    @InjectView(R.id.bankruptcy_segment) SegmentedGroup bankruptcySegment;
    @InjectView(R.id.felon_segment) SegmentedGroup felonSegment;

    private Answer lostCourt = new Answer();
    private Answer lawsuit = new Answer();
    private Answer foreclosed = new Answer();
    private Answer bankruptcy = new Answer();
    private Answer felon = new Answer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_app3, container, false);
        ButterKnife.inject(this, view);

        lostCourtSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        lawsuitSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        foreclosedSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        bankruptcySegment.setTintColor(getActivity().getResources().getColor(R.color.blue));
        felonSegment.setTintColor(getActivity().getResources().getColor(R.color.blue));

        lostCourtSegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(lostCourt));
        lawsuitSegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(lawsuit));
        foreclosedSegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(foreclosed));
        bankruptcySegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(bankruptcy));
        felonSegment.setOnCheckedChangeListener(new YesNoCheckChangedListener(felon));

        return view;
    }

    @OnClick(R.id.next_button)
    public void nextButtonTapped() {
        saveFormValuesToCosignerApplication();
        getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp4.class));
    }

    private void saveFormValuesToCosignerApplication() {
        CosignerApplication.INSTANCE.setLostCourtJudgement(lostCourt.ans);
        CosignerApplication.INSTANCE.setHadLawsuitFiled(lawsuit.ans);
        CosignerApplication.INSTANCE.setForeclosedUpon(foreclosed.ans);
        CosignerApplication.INSTANCE.setDeclaredBankruptcy(bankruptcy.ans);
        CosignerApplication.INSTANCE.setIsFelon(felon.ans);
    }

}
