package com.rentalgeek.android.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorArray;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.PaymentBackend;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.activity.ActivityGeekScore;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentPayment extends GeekBaseFragment implements Validator.ValidationListener {

    private static final String TAG = "FragmentPayment";
    private Validator validator;
    AppPrefes appPref;

    @InjectView(R.id.verify_card) Button verify_card;

    @Required(order = 1, message = "Please enter a valid card")
    @TextRule(order = 2, minLength = 16, maxLength = 16, message = "Please enter a 16 digit card number")
    @InjectView(com.rentalgeek.android.R.id.editTextCardNumber) EditText cardNo;

    @Required(order = 3, message = "Please enter a valid card name")
    @InjectView(R.id.editTextNameOnCard) EditText cardName;

    @Select(order = 6, message = "Please select a valid month")
    @InjectView(R.id.ed_mm) Spinner ed_mm;

    @Select(order = 6, message = "Please enter a valid year")
    @InjectView(R.id.ed_yyyy) Spinner edYYYY;

    @Required(order = 8, message = "Please enter a valid cvv")
    @TextRule(order = 9, minLength = 3, maxLength = 3, message = "Please enter a valid cvv")
    @InjectView(R.id.editTextCVV) EditText edCvv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_info_dialog, container, false);
        ButterKnife.inject(this, v);
        appPref = new AppPrefes(getActivity(), "rentalgeek");
        CheckPaymentf();
        return v;
    }

    private void CheckPaymentf() {
        GlobalFunctions.getApiCall(getActivity(), ApiManager.getApplicants(appPref.getData("Uid")),
                AppPreferences.getAuthToken(),
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
                            PaymentCheckParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }

    private void PaymentCheckParse(String response) {
        try {
            LoginBackend detail = new Gson().fromJson(response, LoginBackend.class);
            SessionManager.Instance.onUserLoggedIn(detail);

            if (detail.user.payment) {
                toast("You have already paid");
                appPref.SaveData("hasPay", "yes");
                SessionManager.Instance.setPayed(true);
                verify_card.setEnabled(false);
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    public void paymentParse(String response) {
        PaymentBackend detail = new Gson().fromJson(response, PaymentBackend.class);

        if (detail != null && detail.transaction != null) {
            toast("Payment successful, transaction ID" + detail.transaction.transaction_id);

            SessionManager.Instance.setPayed(true);

            if (!SessionManager.Instance.hasProfile()) {
                profileAlert("Your payment was successful. Please complete your profile in order to apply.");
                Navigation.navigateActivity(getActivity(), ActivityCreateProfile.class);
            } else {
                Navigation.navigateActivity(getActivity(), ActivityGeekScore.class);
            }
        }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            toast(message);

        }
    }

    @Override
    public void onValidationSucceeded() {
        makePayment();
    }

    private void makePayment() {
        String url = ApiManager.getTransactions();

        RequestParams params = new RequestParams();
        params.put("card[name_on_card]", cardName.getText().toString().trim());
        params.put("card[card_no]", cardNo.getText().toString().trim());
        params.put("card[cvv]", edCvv.getText().toString().trim());
        params.put("card[mm]", ed_mm.getSelectedItem().toString().trim());
        params.put("card[yyyy]", edYYYY.getSelectedItem().toString().trim());
        params.put("card[user_id]", appPref.getData("Uid"));

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
                            paymentParse(content);
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

    @OnClick(R.id.verify_card)
    public void Payment() {
        validator.validate();
    }

    public void profileAlert(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(message);
        builder1.setTitle("Alert");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Go to Profile",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        Navigation.navigateActivity(getActivity(), ActivityCreateProfile.class);
                    }
                });

        builder1.setNegativeButton("Home",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
