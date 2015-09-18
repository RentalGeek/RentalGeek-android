package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LeaseResponse;
import com.rentalgeek.android.backend.model.Lease;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FragmentPaymentConfirmation extends GeekBaseFragment {

    private static final String TAG = FragmentPaymentConfirmation.class.getSimpleName();

    @InjectView(R.id.textViewPaymentSummary)
    TextView textViewPaymentSummary;

    @InjectView(R.id.layoutProcessPayment)
    LinearLayout layoutProcessPayment;

    private Lease currentLease;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_payment_confirmation, container, false);
        ButterKnife.inject(this, v);

        evaluatePendingPayments();

        return v;
    }

    protected void evaluatePendingPayments() {
        if (SessionManager.Instance.hasCompletedLeaseId()) {
            layoutProcessPayment.setVisibility(View.VISIBLE);
            fetchPaymentHistory(SessionManager.Instance.getCurrentUser().completed_lease_id);
        } else {
            layoutProcessPayment.setVisibility(View.GONE);
            textViewPaymentSummary.setText(R.string.fragment_payment_nonedue);
        }
    }

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

    protected void bindLeasePayment(Lease lease) {

        textViewPaymentSummary.setText(Html.fromHtml(RentalGeekApplication.getResourceString(R.string.fragment_payment_totaldue, lease.first_months_rent, lease.security_deposit)));
    }

    protected void fetchPaymentHistory(String leaseId) {

        String url = ApiManager.getLease(leaseId);

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
                            LeaseResponse leaseResponse = (new Gson()).fromJson(content, LeaseResponse.class);
                            if (leaseResponse != null && leaseResponse.lease != null) {
                                currentLease = leaseResponse.lease;
                                bindLeasePayment(leaseResponse.lease);
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
