package com.rentalgeek.android.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.model.YesNoAnswer;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp4;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.OkAlert;
import com.rentalgeek.android.utils.ResponseParser;
import com.rentalgeek.android.utils.YesNoCheckChangedListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by rajohns on 9/19/15.
 */
public class FragmentCosignerApp3 extends GeekBaseFragment {

    @InjectView(R.id.lost_court_segment)
    SegmentedGroup lostCourtSegment;
    @InjectView(R.id.lawsuit_segment)
    SegmentedGroup lawsuitSegment;
    @InjectView(R.id.foreclosed_segment)
    SegmentedGroup foreclosedSegment;
    @InjectView(R.id.bankruptcy_segment)
    SegmentedGroup bankruptcySegment;
    @InjectView(R.id.felon_segment)
    SegmentedGroup felonSegment;

    private YesNoAnswer lostCourt = new YesNoAnswer();
    private YesNoAnswer lawsuit = new YesNoAnswer();
    private YesNoAnswer foreclosed = new YesNoAnswer();
    private YesNoAnswer bankruptcy = new YesNoAnswer();
    private YesNoAnswer felon = new YesNoAnswer();

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
        RequestParams params = new RequestParams();
        params.put("cosigner_profile[step]", "legal_history");
        params.put("cosigner_profile[ever_lost_a_court_judgement]", String.valueOf(lostCourt.ans));
        params.put("cosigner_profile[ever_been_foreclosed_upon]", String.valueOf(foreclosed.ans));
        params.put("cosigner_profile[ever_had_lawsuit_filed]", String.valueOf(lawsuit.ans));
        params.put("cosigner_profile[ever_declared_bankruptcy]", String.valueOf(bankruptcy.ans));
        params.put("cosigner_profile[is_felon]", String.valueOf(felon.ans));

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
                getActivity().startActivity(new Intent(getActivity(), ActivityCosignerApp4.class));
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
