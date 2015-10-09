package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.model.YesNoAnswer;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.CosignerDestinationLogic;
import com.rentalgeek.android.utils.ResponseParser;
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
            RequestParams params = new RequestParams();
            params.put("cosigner_profile[step]", "employment_info");
            params.put("cosigner_profile[employer_name]", employer);
            params.put("cosigner_profile[employment_position]", position);
            params.put("cosigner_profile[monthly_income]", income);
            params.put("cosigner_profile[intend_to_cover_rent]", String.valueOf(coverRent.ans));

            GlobalFunctions.putApiCall(getActivity(), ApiManager.cosignerProfilesUrl(SessionManager.Instance.getCurrentUser().cosigner_profile_id), params, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    showProgressDialog(R.string.dialog_msg_loading);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hideProgressDialog();
                }

                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    CosignerDestinationLogic.INSTANCE.navigateToNextCosignActivity(getActivity());
                }

                @Override
                public void onFailure(Throwable ex, String failureResponse) {
                    super.onFailure(ex, failureResponse);
                    ResponseParser.ErrorMsg errorMsg = new ResponseParser().humanizedErrorMsg(failureResponse);
                    OkAlert.show(getActivity(), errorMsg.title, errorMsg.msg);
                }
            });
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
