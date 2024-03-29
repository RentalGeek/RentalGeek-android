package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.model.RoommatePayment;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.rentalgeek.android.constants.IntentKey.*;


public class FragmentPaymentConfirmation extends GeekBaseFragment {

    private static final String TAG = FragmentPaymentConfirmation.class.getSimpleName();

    @InjectView(R.id.textViewAmountPaid) TextView textViewAmountPaid;
    @InjectView(R.id.layoutPaymentHistory) LinearLayout layoutPaymentHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_confirmation, container, false);
        ButterKnife.inject(this, v);

        int currentLeaseId = getArguments().getInt(LEASE_ID);
        double amountPaid = getArguments().getDouble(TOTAL_DUE);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        textViewAmountPaid.setText(format.format(amountPaid));

        fetchPaymentHistory(currentLeaseId);

        return v;
    }

    @OnClick(R.id.buttonHome)
    public void clickButtonHome() {
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    protected void bindRoommatePayment(List<RoommatePayment> roommatePayments) {
        layoutPaymentHistory.removeAllViews();

        if (!ListUtils.isNullOrEmpty(roommatePayments)) {
            for (int i = 0; i < roommatePayments.size(); i++) {
                LinearLayout layoutListItemRoommatePayment = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.layout_listitem_roommate_payment, null);

                TextView textViewRoommateName = getView(R.id.textViewRoommateName, layoutListItemRoommatePayment);
                TextView textViewPaymentStatus = getView(R.id.textViewPaymentStatus, layoutListItemRoommatePayment);

                textViewRoommateName.setText(Html.fromHtml("Payment from <b>" + roommatePayments.get(i).full_name + "</b>"));

                String status = roommatePayments.get(i).paid ? "COMPLETE" : "NOT RECEIVED";
                textViewPaymentStatus.setText(status);
                textViewPaymentStatus.setTextColor(getResources().getColor(roommatePayments.get(i).paid ? R.color.green : R.color.blue));

                layoutPaymentHistory.addView(layoutListItemRoommatePayment);
            }
        }
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
                        Type listType = new TypeToken<List<RoommatePayment>>() {
                        }.getType();
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
