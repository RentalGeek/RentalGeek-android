package com.rentalgeek.android.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.rentalgeek.android.backend.CheckPayment;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentPayment extends GeekBaseFragment implements Validator.ValidationListener {

	private static final String TAG = "FragmentPayment";

	private Validator validator;
	AppPrefes appPref;

	@InjectView(R.id.verify_card)
	Button verify_card;

	@Required(order = 1, message = "Please enter a valid card")
	@TextRule(order = 2, minLength = 16, maxLength = 16, message = "Please enter a 16 digit card number")
	@InjectView(com.rentalgeek.android.R.id.editTextCardNumber)
	EditText cardNo;

	@Required(order = 3, message = "Please enter a valid card name")
	@InjectView(R.id.editTextNameOnCard)
	EditText cardName;

	// @Required(order = 4, message = "Please enter a valid month")
	// @TextRule(order = 4, minLength = 2, maxLength = 2, message =
	// "Please enter a valid month")
	// @Regex(order = 5, pattern = "0[1-9]|1[0-2]", message =
	// "Please enter a valid month")
	// @InjectView(R.id.ed_mm)
	// EditText ed_mm;

	@Select(order = 6, message = "Please select a valid month")
	@InjectView(R.id.ed_mm)
	Spinner ed_mm;

	// @Required(order = 6, message = "Please enter a valid year")
	// @TextRule(order = 7, minLength = 4, maxLength = 4, message =
	// "Please enter a valid year")
	@Select(order = 6, message = "Please enter a valid year")
	@InjectView(R.id.ed_yyyy)
	Spinner edYYYY;

	@Required(order = 8, message = "Please enter a valid cvv")
	@TextRule(order = 9, minLength = 3, maxLength = 3, message = "Please enter a valid cvv")
	@InjectView(R.id.editTextCVV)
	EditText edCvv;

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

		// The done button click from a keyboard
		//KeyListener();

		CheckPaymentf();

		return v;
	}

//	private void KeyListener() {
//
//		edCvv.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId,
//										  KeyEvent event) {
//
//				if (actionId != EditorInfo.IME_ACTION_DONE)
//					return false;
//				//hidekey();
//				validator.validate();
//				return true;
//
//			}
//		});
//
//	}

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
							PaymentCheckParseNew(content);
						} catch (Exception e) {
							AppLogger.log(TAG, e);
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}
				});

	}

	private void PaymentCheckParseNew(String response) {


		try {
			LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);

			if (detail.user.payment) {
				toast("You have already paid");
				appPref.SaveData("hasPay", "yes");
				SessionManager.Instance.setPayed(true);
				verify_card.setEnabled(false);
			} else {

			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

	}

	public void PaymentCheckParse(String response) {


		System.out.println("the payment check response " + response);

		CheckPayment detail = (new Gson()).fromJson(response, CheckPayment.class);

		if (detail.transactions.size() > 0) {
			toast("You have already paid");
            SessionManager.Instance.setPayed(true);
			verify_card.setEnabled(false);
		}

	}

	private List getErroList(List<com.rentalgeek.android.backend.ErrorArray.Error> al) {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < al.size(); i++) {
			list.add(al.get(i).message);
		}

		return list;

	}

	private void alertList(String add) {

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				getActivity());

		builderSingle.setTitle("Error");
		builderSingle.setMessage(add);
		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.show();

	}

	public void paymentParse(String response) {

		PaymentBackend detail = (new Gson()).fromJson(response, PaymentBackend.class);

		if (detail != null && detail.transaction != null) {

			toast("Payment successful, transaction ID" + detail.transaction.transaction_id);

            SessionManager.Instance.setPayed(true);

			if (!SessionManager.Instance.hasProfile()) {
				profileAlert("Your payment is success. Please complete your profile in order to apply.");
				// nextfragment(new FragmentProfile(), false, R.id.container);
			} else {
				Navigation.navigateActivity(getActivity(), ActivityGeekScore.class);
				//nextfragment(new FragmentFinalGeekScore(), false, R.id.container);
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
		//asynkhttp(params, 1, url, AppPreferences.getAuthToken(), true);
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
						//nextfragment(new FragmentProfile(), false, R.id.container);
                        Navigation.navigateActivity(getActivity(), ActivityCreateProfile.class);
					}
				});

		builder1.setNegativeButton("Home",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();

						//nextfragment(new FragmentListViewDetails(), false ,R.id.container);
					}
				});
		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

}
