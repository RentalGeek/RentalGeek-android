package com.rentalgeek.android.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.ErrorApi;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.LoginBackend.user;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentVerifyAccount extends LuttuBaseAbstract implements ValidationListener{

    private static final String TAG = "FragmentVerifyAccount";

    /**
	 * @author george
	 * 
	 * @purpose This class verify the user who is going to pay is acutally the orginal user 
	 */
	
	
	
    @Required(order=1,message="Please enter password")
    @InjectView(R.id.verify_account)
    EditText verify_password;
	 
	 private Validator validator;
	 AppPrefes appPref;
	
	
	@InjectView(R.id.face_verify)
	ImageView face_verify;
	
	@InjectView(R.id.goog_verify)
	ImageView goog_verify;
	
	@InjectView(R.id.link_verify)
	ImageView link_verify;
	
	@InjectView(R.id.rent_verify)
	ImageView rent_verify;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_verify_identity, container, false);

		
		ButterKnife.inject(this,v);
		validator = new Validator(this);
		validator.setValidationListener(this);
		appPref=new AppPrefes(getActivity(), "rentalgeek");
		
		CheckWhichLogin();
		
		//method which listen to the kayboard done clicks
		actionDonekeyListener();
		
		return v;

	}
	
	private void CheckWhichLogin() {

		
		if(appPref.getData("socialid_link").equals(""))
		{
			link_verify.setVisibility(View.GONE);
		}
		
		if(appPref.getData("socialid_goog").equals(""))
		{
			goog_verify.setVisibility(View.GONE);
		}
		
		if(appPref.getData("socialid_fb").equals(""))
		{
			face_verify.setVisibility(View.GONE);
		}
		
		if(appPref.getData("norm_log").equals(""))
		{
			rent_verify.setVisibility(View.GONE);
		}
		
	}

	private void actionDonekeyListener() {

		
		verify_password.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				
				if (actionId  != EditorInfo.IME_ACTION_DONE)
					return false;
				hidekey();
				validator.validate();
				return true;
			}
		});
		
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void parseresult(String response, boolean success, int value) {


		
		switch (value) {
		case 1:
			NormalLogin(response);
			break;
		case 2:
			FacebookParse(response);
			break;
		case 3:
			GoogleParse(response);
			break;
		case 4:
			LinkedParse(response);
			break;
		default:
			break;
		}
	}

	private void LinkedParse(String response) {

		nextfragment(new FragmentPayment(), false, R.id.container);
	}

	private void GoogleParse(String response) {

		nextfragment(new FragmentPayment(), false, R.id.container);
		
	}

	private void FacebookParse(String response) {

		nextfragment(new FragmentPayment(), false, R.id.container);
	}

	@Override
	public void error(String response, int value) {

		
		ErrorApi detail = (new Gson()).fromJson(response,
				ErrorApi.class);
		
		if(!detail.success)
		{
			DialogManager.showCrouton(getActivity(), detail.message);
		}

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

		
		callNormalLogin();
		
	}
	
	private void callNormalLogin() {

		
		RequestParams params = new RequestParams();
		params.put("user[email]", appPref.getData("email"));
		params.put("user[password]", verify_password.getText().toString().trim());
		asynkhttp(params, 1, ApiManager.getSignin(), AppPreferences.getAuthToken(), true);
		
	}

	@OnClick(R.id.rent_verify)
	public void rentVerify()
	{
		validator.validate();
	}
	
	private void NormalLogin(String response) {

		try {

			System.out.println("responseresponse" + response);
			LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);

			user appin = detail.user;
			log("my id is " + appin.id);
			log("my id is " + detail.user.id);

			String appid = String.valueOf(detail.user.id);
			System.out.println("my id is " + appid);

			appPref.SaveData("Uid", appid);
			appPref.SaveData("email", detail.user.email);
			
			nextfragment(new FragmentPayment(), false, R.id.container);
		 
			 
			 
		} catch (Exception e) {
            AppLogger.log(TAG, e);
		}
	}
	
	
	
	private void callFacebookLink() {

		RequestParams params = new RequestParams();
		params.put("provider[uid]", appPref.getData("socialid_fb"));
		params.put("provider[provider]", "Facebook");
		params.put("provider[email]", appPref.getData("socialemail_fb"));
		params.put("provider[name]",  appPref.getData("socialname_fb"));
		asynkhttp(params, 2, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

	}
	
	
	private void callGoogleLink() {

		RequestParams params = new RequestParams();
		params.put("provider[uid]", appPref.getData("socialid_goog"));
		params.put("provider[provider]", "Google+");
		params.put("provider[email]", appPref.getData("socialemail_goog"));
		params.put("provider[name]", appPref.getData("socialname_goog"));
		asynkhttp(params, 3, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

	}
	
	
	private void callLinkedLink() {
 
		RequestParams params = new RequestParams();
		params.put("provider[uid]", appPref.getData("socialid_link"));
		params.put("provider[provider]", "LinkedIn+");
		params.put("provider[email]", appPref.getData("socialemail_link"));
		params.put("provider[name]",  appPref.getData("socialname_link"));
		asynkhttp(params, 4, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

	}

	@OnClick(R.id.face_verify)
	public void FaceBookClick()
	{
		if(!appPref.getData("socialid_fb").equals(""))
		{
			callFacebookLink();
		}
		else
		{
			DialogManager.showCrouton(getActivity(), "Session out, please login to continue");

			PersistentCookieStore mCookieStore=new PersistentCookieStore(getActivity());
			mCookieStore.clear();

			//TODO: implement this in new facebook lib
//			Session session = Session.getActiveSession();
//			if(session!=null)
//			session.closeAndClearTokenInformation();

			appPref.deleteAll();
			appPref.SaveData("first", "");
			getActivity().finish();
			Intent intent = new Intent(getActivity(),FragmentSignIn.class);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.three_, R.anim.four_);
		}
		
	}
	
	@OnClick(R.id.goog_verify)
	public void GoogleClick()
	{
		if(!appPref.getData("socialid_goog").equals(""))
		{
			callGoogleLink();
		}
		else
		{
            DialogManager.showCrouton(getActivity(), "Session out, please login to continue");

			PersistentCookieStore mCookieStore=new PersistentCookieStore(getActivity());
			mCookieStore.clear();

//			Session session = Session.getActiveSession();
//			if(session!=null)
//			session.closeAndClearTokenInformation();

			appPref.deleteAll();
			appPref.SaveData("first", "");

			getActivity().finish();
			Intent intent = new Intent(getActivity(),FragmentSignIn.class);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.three_, R.anim.four_);
		}
	}
	
	
	@OnClick(R.id.link_verify)
	public void LinkedClick()
	{
		if(!appPref.getData("socialid_link").equals(""))
		{
			callLinkedLink();
		}
		else
		{
	//		toast("");
//			PersistentCookieStore mCookieStore=new PersistentCookieStore(getActivity());
//			mCookieStore.clear();
//			Session session = Session.getActiveSession();
//			if(session!=null)
//			session.closeAndClearTokenInformation();
//			appPref.deleteAll();
//			appPref.SaveData("first", "");
//			getActivity().finish();
//			Intent intent = new Intent(getActivity(),SignIn.class);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			getActivity().overridePendingTransition(
//					R.anim.three_, R.anim.four_);
		}
	}
	
	
	@OnClick(R.id.infoclick2)
	public void infoclick2()
	{
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogInner);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.apply_confirm_dialog);
		
		TextView ok=(TextView) dialog.findViewById(R.id.ok_apply_dialog);
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();

	}
	
	
	
	
	@OnClick(R.id.infoclick1)
	public void infoclick1()
	{
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogInner);

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
