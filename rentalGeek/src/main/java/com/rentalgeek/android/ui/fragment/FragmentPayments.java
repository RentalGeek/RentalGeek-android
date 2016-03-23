package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorArray;
import com.rentalgeek.android.backend.LeaseResponse;
import com.rentalgeek.android.backend.PaymentsBackend;
import com.rentalgeek.android.backend.model.Lease;
import com.rentalgeek.android.constants.IntentKey;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityPaymentConfirmation;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import java.text.NumberFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FragmentPayments extends GeekBaseFragment implements Validator.ValidationListener {

    private static final String TAG = FragmentPayments.class.getSimpleName();
    private Validator validator;

    @InjectView(R.id.textViewPaymentSummary) TextView textViewPaymentSummary;
    @InjectView(R.id.textViewPaymentTotal) TextView textViewPaymentTotal;
    @InjectView(R.id.layoutProcessPayment) LinearLayout layoutProcessPayment;

    @Required(order = 1, message = "Please enter a valid card")
    @TextRule(order = 2, minLength = 16, maxLength = 16, message = "Please enter a 16 digit card number")
    @InjectView(com.rentalgeek.android.R.id.editTextCardNumber)
    EditText editTextCardNumber;

    @Required(order = 3, message = "Please enter a valid card name")
    @InjectView(R.id.editTextNameOnCard)
    EditText editTextNameOnCard;

    @Select(order = 6, message = "Please select a valid month")
    @InjectView(R.id.ed_mm)
    Spinner ed_mm;

    @Select(order = 6, message = "Please enter a valid year")
    @InjectView(R.id.ed_yyyy)
    Spinner edYYYY;

    @Required(order = 8, message = "Please enter a valid cvv")
    @TextRule(order = 9, minLength = 3, maxLength = 3, message = "Please enter a valid cvv")
    @InjectView(R.id.editTextCVV)
    EditText editTextCVV;

    private Lease currentLease;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payments, container, false);
        ButterKnife.inject(this, v);

        evaluatePendingPayments();

        return v;
    }

    protected void evaluatePendingPayments() {
        if (SessionManager.Instance.hasCompletedLeaseId()) {
            layoutProcessPayment.setVisibility(View.VISIBLE);
            fetchLeaseAmountDue(SessionManager.Instance.getCurrentUser().completed_lease_id);
        } else {
            layoutProcessPayment.setVisibility(View.GONE);
            textViewPaymentSummary.setText(R.string.fragment_payment_nonedue);
        }
    }

    @OnClick(R.id.buttonPaymentSubmit)
    public void clickButtonPaymentSubmit() {
        validator.validate();
    }

    private void KeyListener() {
        editTextCVV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }

                validator.validate();
                return true;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    protected void bindLeasePayment(Lease lease) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        textViewPaymentTotal.setText(format.format(lease.total_due));
        textViewPaymentSummary.setText(Html.fromHtml(RentalGeekApplication.getResourceString(R.string.fragment_payment_totaldue, lease.first_months_rent, lease.security_deposit)));
    }

    protected void fetchLeaseAmountDue(String leaseId) {
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
                    DialogManager.showCrouton(activity, failureResponse);
                }

                @Override
                public void onAuthenticationFailed() {

                }
            });
    }

    @Override
    public void onValidationSucceeded() {
        makePayment();
    }

    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        String message = rule.getFailureMessage();
        if (view instanceof EditText) {
            view.requestFocus();
            ((EditText) view).setError(message);
        } else {
            toast(message);

        }
    }

    private void makePayment() {
        String url = ApiManager.getPayments();

        RequestParams params = new RequestParams();
        params.put("payment[lease_id]", String.valueOf(currentLease.id));
        params.put("payment[amount]", String.valueOf(currentLease.total_due));
        params.put("payment[name_on_card]", editTextNameOnCard.getText().toString().trim());
        params.put("payment[card_number]", editTextCardNumber.getText().toString().trim());
        params.put("payment[cvv]", editTextCVV.getText().toString().trim());
        params.put("payment[exp_month]", ed_mm.getSelectedItem().toString().trim());
        params.put("payment[exp_year]", edYYYY.getSelectedItem().toString().trim());
        params.put("payment[user_id]", SessionManager.Instance.getCurrentUser().id);

        GlobalFunctions.postApiCall(activity, url,
            params, AppPreferences.getAuthToken(),
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
                        PaymentsBackend detail = (new Gson()).fromJson(content, PaymentsBackend.class);

                        if (detail != null && detail.payment != null) {
                            Bundle args = new Bundle();
                            args.putDouble(IntentKey.TOTAL_DUE, currentLease.total_due);
                            args.putInt(IntentKey.LEASE_ID, currentLease.id);
                            Navigation.navigateActivity(activity, ActivityPaymentConfirmation.class, args, false);
                        }

                    } catch (Exception e) {
                        AppLogger.log(TAG, e);
                    }
                }

                @Override
                public void onFailure(Throwable ex, String failureResponse) {
                    super.onFailure(ex, failureResponse);

                    ErrorArray errorResponse = (new Gson()).fromJson(failureResponse, ErrorArray.class);

                    if (errorResponse != null && !ListUtils.isNullOrEmpty(errorResponse.errors)) {
                        DialogManager.showCrouton(activity, errorResponse.errors.get(0).message);
                    }

                }

                @Override
                public void onAuthenticationFailed() {

                }
            });
    }

}
