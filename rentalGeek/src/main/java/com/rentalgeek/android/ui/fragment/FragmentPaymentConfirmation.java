package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.model.RoommatePayment;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FragmentPaymentConfirmation extends GeekBaseFragment {

    private static final String TAG = FragmentPaymentConfirmation.class.getSimpleName();

    @InjectView(R.id.textViewAmountPaid)
    TextView textViewAmountPaid;

//    @InjectView(R.id.layoutProcessPayment)
//    LinearLayout layoutProcessPayment;

    private int currentLeaseId;
    private double amountPaid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_payment_confirmation, container, false);
        ButterKnife.inject(this, v);

        currentLeaseId = getArguments().getInt(Common.KEY_LEASE_ID);
        amountPaid = getArguments().getDouble(Common.KEY_TOTAL_DUE);

        fetchPaymentHistory(currentLeaseId);

        return v;
    }

//    protected void evaluatePendingPayments() {
//        if (SessionManager.Instance.hasCompletedLeaseId()) {
//            layoutProcessPayment.setVisibility(View.VISIBLE);
//            fetchPaymentHistory(SessionManager.Instance.getCurrentUser().completed_lease_id);
//        } else {
//            layoutProcessPayment.setVisibility(View.GONE);
//            textViewPaymentSummary.setText(R.string.fragment_payment_nonedue);
//        }
//    }

    @OnClick(R.id.buttonHome)
    public void clickButtonHome() {
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
//        if (ApiManager.currentUser != null && ApiManager.currentUser.roommate_group_id != null)
//           fetchRoommateGroups(ApiManager.currentUser.roommate_group_id);
    }

    protected void bindRoommatePayment(List<RoommatePayment> roommatePayments) {

        //textViewPaymentSummary.setText(Html.fromHtml(RentalGeekApplication.getResourceString(R.string.fragment_payment_totaldue, lease.first_months_rent, lease.security_deposit)));
    }

    protected void fetchPaymentHistory(int leaseId) {

        String url = ApiManager.getLeaseRoommatePayments(String.valueOf(leaseId));

        GlobalFunctions.getApiCall(activity, url, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        showProgressDialog(R.string.dialog_msg_loading);
                    }

                    @Override
                    public void onFinish() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            String data = content;
                            AppLogger.log(TAG, content);
                            Type listType = new TypeToken<List<RoommatePayment>>(){}.getType();
                            List<RoommatePayment> response = (new Gson()).fromJson(content, listType);
                            if (response != null && response.size() > 0) {

                                bindRoommatePayment(response);
                            }
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }
}