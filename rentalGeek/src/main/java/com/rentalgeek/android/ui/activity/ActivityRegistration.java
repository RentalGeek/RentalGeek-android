package com.rentalgeek.android.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;
import com.rentalgeek.android.utils.Loading;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ActivityRegistration extends GeekBaseActivity implements ValidationListener {

	private static final String TAG = "ActivityRegistration";

	public ActivityRegistration() {
		super(false, false, false);
	}

	@Required(order = 1, message = "Please enter valid email")
	@Email(order = 2, message = "Please enter valid email")
	@InjectView(R.id.email_add_regis)
	EditText email_add_regis;

	private YoYo.YoYoString animation_obj;

//	@InjectView(R.id.terms)
//	CheckBox terms;

//	@InjectView(R.id.containerregis)
//	FrameLayout prog;

	@InjectView(R.id.terms_text)
	TextView terms_text;

	@Password(order = 3)
	@TextRule(order = 5, minLength = 8, message = "Password length should be 8 characters")
	@Required(order = 4, message = "Please enter pasword")
	@InjectView(R.id.password_regis)
	EditText password_regis;

	@ConfirmPassword(order = 6)
	@InjectView(R.id.confirm_password_regis)
	EditText confirm_password_regis;

	private Validator validator;

	@InjectView(R.id.create_account)
	Button create_account;

	Loading load;

	AppPrefes appPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		ButterKnife.inject(this);
		terms_text.setSingleLine(false);
		terms_text.setText(Html.fromHtml("By creating a RentalGeek account,\n you agree to our <u>Terms & Conditions</u>"));
		validator = new Validator(this);
		validator.setValidationListener(this);
		load = new Loading(ActivityRegistration.this);
		appPref = new AppPrefes(getApplicationContext(), "rentalgeek");
	}

	@OnClick(R.id.create_account)
	public void CreateAccnt(View v) {
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		validator.validate();
	}

	private void callRegisterApi(String a, String b, String c) {

        final FragmentActivity activity = this;
		//new GetSSl().getssl(client);
		RequestParams params = new RequestParams();
		params.put("user[email]", a);
		params.put("user[password]", b);
		params.put("user[confirm_password]", c);

		GlobalFunctions.postApiCall(this, ApiManager.regis_link, params, AppPreferences.getAuthToken(),
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
                        LoginBackend detail = null;
						try {
                            detail = (new Gson()).fromJson(content.toString(), LoginBackend.class);
							//detail = (new Gson()).fromJson(content.toString(), RegistrationBackend.class);

							//prog.setVisibility(View.INVISIBLE);
							System.out.println("the registration response " + content);

							if (detail.user != null) {
								LoginBackend.User user = detail.user;

                                SessionManager.Instance.onUserLoggedIn(user);

                                Navigation.navigateActivity(activity, ActivityHome.class, true);
                                //toast("Registration Successful, Please Login to continue");

//								new CountDownTimer(1000, 1000) {
//
//									@Override
//									public void onTick(long millisUntilFinished) { }
//
//									@Override
//									public void onFinish() {
//										finish();
//									}
//								}.start();
							} else if (detail.errors != null && !ListUtils.isNullOrEmpty(detail.errors.email)) {
								toast(detail.errors.email.get(0).toString());
							}

						} catch (Exception e) {
							AppLogger.log(TAG, e);
							if (detail != null && detail.errors != null && detail.errors.email.size() > 0)
								toast(detail.errors.email.get(0).toString());
							else
								toast("No Connection");
						}
					}

				});


	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {

		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			// Toast.makeText(getActivity(), message,
			// Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onValidationSucceeded() {

		callRegisterApi(email_add_regis.getText().toString(),
				password_regis.getText().toString(),
				confirm_password_regis.getText().toString());

//		if (terms.isChecked()) {
//			if (con.isConnectingToInternet()) {
//				callRegisterApi(email_add_regis.getText().toString(),
//						password_regis.getText().toString(),
//						confirm_password_regis.getText().toString());
//			} else {
//				toast("No Connection");
//			}
//		} else {
//			toast("Please accept terms and conditions");
//		}

	}

	public void toast(String message) {
		DialogManager.showCrouton(this, message);
//		Crouton crouton = Crouton.makeText(ActivityRegistration.this, message,
//				Style.CONFIRM);
//		crouton.show();
	}

	@OnClick(R.id.terms_text)
	public void infoclick1() {
		final Dialog dialog = new Dialog(ActivityRegistration.this,
				R.style.MyDialogInner);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.terms_dialog);

		WebView wv;
		TextView close;
		wv = (WebView) dialog.findViewById(R.id.terms_web);
		close = (TextView) dialog.findViewById(R.id.close);
		wv.loadUrl("file:///android_asset/terms.html");

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

	}

}
