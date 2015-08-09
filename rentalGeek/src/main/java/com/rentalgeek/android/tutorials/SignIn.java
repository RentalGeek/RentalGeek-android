package com.rentalgeek.android.tutorials;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.rentalgeek.android.R;
import com.rentalgeek.android.backend.ErrorApi;
import com.rentalgeek.android.backend.ForgotError;
import com.rentalgeek.android.backend.GoogleBackend;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.backend.LoginBackend.applicant;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.homepage.HomeActivity;
import com.rentalgeek.android.homepage.Registration;
import com.rentalgeek.android.utils.ConnectionDetector;
import com.rentalgeek.android.utils.StaticClass;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.linkedin.platform.LISessionManager;
import com.loopj.android.http.RequestParams;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.luttu.fragmentutils.VolleyForAll;
import com.luttu.fragmentutils.VolleyOnResponseListener;

public class SignIn extends LuttuBaseAbstract implements ConnectionCallbacks,
		OnConnectionFailedListener {

	// G+-------------

	public static final int RC_SIGN_IN = 0;
	private static final String TAG = "MainActivity";
	ProfileTable profdets;
	private YoYo.YoYoString animation_obj;

	private static final int PROFILE_PIC_SIZE = 400;

	private GoogleApiClient mGoogleApiClient;
	AppPrefes appPref;

	private boolean mIntentInProgress;

	private boolean mSignInClicked;
	ConnectionDetector con;

	private ConnectionResult mConnectionResult;

	@InjectView(R.id.btn_sign_in)
	SignInButton btnSignIn;

	@InjectView(R.id.create_aacnt)
	TextView create_aacnt;

	@InjectView(R.id.google_plus)
	ImageView google_plus;

	@InjectView(R.id.facebook_lays)
	ImageView facebook_lay;

	@InjectView(R.id.linked_lay)
	ImageView linked_lay;

	// ---------------

	@InjectView(R.id.ed_username)
	EditText ed_username;

	@InjectView(R.id.authButton)
	LoginButton authButton;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private UiLifecycleHelper uiHelper;

	@InjectView(R.id.ed_password)
	EditText ed_password;

	VolleyForAll volley;

	public static SignIn newInstance() {
		SignIn fragment = new SignIn();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		con = new ConnectionDetector(getActivity());
		if (appPref.getData("first").equals("")) {

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.sigin_latest, container, false);
		// signin();
		ButterKnife.inject(this, v);
		create_aacnt.setText(Html
				.fromHtml("Not a member? <u>Create Account</u>"));

		// facebook essentials
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("public_profile", "email",
				"user_birthday"));

		// google plus initialization
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();

		// Linked in needs

		return v;

	}

	private void signin(String a, String b) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("applicant[email]", a);
		params.put("applicant[password]", b);
		asynkhttp(params, 1, StaticClass.headlink + "/applicants/sign_in.json",
				true);

	}

	// @OnClick(R.id.sign_button)
	// public void SignIn() {
	//
	// Intent i = new Intent(getActivity(), HomeActivity.class);
	// startActivity(i);
	// getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);
	//
	// }

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void parseresult(String response, boolean success, int value) {
		// TODO Auto-generated method stub

		switch (value) {
		case 1:
			NormalLogin(response);
			break;
		case 2:
			FaceBookLogin(response);
			break;
		case 3:
			googlePlusParse(response);
			break;
		case 4:
			LinkedInParse(response);
			break;
		case 5:
			ForgotMailSentParse(response);
			break;
		}
	}

	private void ForgotMailSentParse(String response) {
		// TODO Auto-generated method stub
		toast("Verification email sent to your mail please check");

	}

	private void LinkedInParse(String response) {
		// TODO Auto-generated method stub

		GoogleBackend detail = (new Gson()).fromJson(response,
				GoogleBackend.class);

		// Applicant appli=detail.applicant;

		String appid = String.valueOf(detail.applicant.id);
		System.out.println("my id is " + appid);

		if (detail.applicant.payment) {
			appPref.SaveIntData("payed", 200);
		}

		if (detail.applicant.profile_id != null) {
			appPref.SaveData("prof_id", detail.applicant.profile_id);
		} else {
			appPref.SaveData("prof_id", "");
		}

		appPref.SaveData("Uid", appid);
		
		com.activeandroid.query.Select select = new com.activeandroid.query.Select();
		List<ProfileTable> profcont = select
				.all()
				.from(ProfileTable.class)
				.execute();

		if (profcont.size() > 0) {
			profdets = new com.activeandroid.query.Select()
					.from(ProfileTable.class)
					.where("uid = ?",
							appPref.getData("Uid"))
					.executeSingle();
			
			profdets.firstname=appPref.getData("socialname_link");
			profdets.lastname=appPref.getData("sociallastname_link");
			profdets.save();
			
		} else {
			profdets = new ProfileTable();
			profdets.uid = appPref
					.getData("Uid");
			profdets.firstname=appPref.getData("socialname_link");
			profdets.lastname=appPref.getData("sociallastname_link");
			profdets.save();
		}
		
		// App logs in
		getActivity().finish();
		appPref.SaveData("first", "logged");
		Intent i = new Intent(getActivity(), HomeActivity.class);
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

	}

	private void googlePlusParse(String response) {
		// TODO Auto-generated method stub
		System.out.println("google response " + response);
		GoogleBackend detail = (new Gson()).fromJson(response,
				GoogleBackend.class);

		// Applicant appli=detail.applicant;

		String appid = String.valueOf(detail.applicant.id);
		System.out.println("my id is " + appid);

		if (detail.applicant.payment) {
			appPref.SaveIntData("payed", 200);
		}

		if (detail.applicant.profile_id != null) {
			appPref.SaveData("prof_id", detail.applicant.profile_id);
		} else {
			appPref.SaveData("prof_id", "");
		}

		appPref.SaveData("Uid", appid);
		
		
		com.activeandroid.query.Select select = new com.activeandroid.query.Select();
		List<ProfileTable> profcont = select
				.all()
				.from(ProfileTable.class)
				.execute();

		if (profcont.size() > 0) {
			profdets = new com.activeandroid.query.Select()
					.from(ProfileTable.class)
					.where("uid = ?",
							appPref.getData("Uid"))
					.executeSingle();
			profdets.firstname=appPref.getData("socialname_goog");
			profdets.lastname=" ";
			profdets.save();
			
		} else {
			profdets = new ProfileTable();
			profdets.uid = appPref
					.getData("Uid");
		
			
			String[] names = appPref.getData("socialname_goog").trim().split(" ");
			
			if(names.length>1)
			{
				profdets.firstname=names[0];
				profdets.lastname=names[1];
				profdets.save();
			}
			else
			{
				profdets.firstname=appPref.getData("socialname_goog");
				profdets.lastname=appPref.getData("sociallastname_goog");
				profdets.save();
			}
			
		}
		
		// App logs in
		
		
		getActivity().finish();
		appPref.SaveData("first", "logged");
		signOutFromGplus();
		Intent i = new Intent(getActivity(), HomeActivity.class);
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

	}

	private void FaceBookLogin(String response) {
		// TODO Auto-generated method stub
		GoogleBackend detail = (new Gson()).fromJson(response,
				GoogleBackend.class);

		// Applicant appli=detail.applicant;

		String appid = String.valueOf(detail.applicant.id);
		System.out.println("my id is " + appid);

		appPref.SaveData("Uid", appid);
		if (detail.applicant.payment) {
			appPref.SaveIntData("payed", 200);
		}

		if (detail.applicant.profile_id != null) {
			appPref.SaveData("prof_id", detail.applicant.profile_id);
		} else {
			appPref.SaveData("prof_id", "");
		}
		
		com.activeandroid.query.Select select = new com.activeandroid.query.Select();
		List<ProfileTable> profcont = select
				.all()
				.from(ProfileTable.class)
				.execute();

		if (profcont.size() > 0) {
			profdets = new com.activeandroid.query.Select()
					.from(ProfileTable.class)
					.where("uid = ?",
							appPref.getData("Uid"))
					.executeSingle();
			
			profdets.firstname=appPref.getData("socialname_fb");
			profdets.lastname=appPref.getData("sociallastname_fb");
			profdets.save();
			
		} else {
			profdets = new ProfileTable();
			profdets.uid = appPref
					.getData("Uid");
			profdets.firstname=appPref.getData("socialname_fb");
			profdets.lastname=appPref.getData("sociallastname_fb");
			profdets.save();
		}
		
		getActivity().finish();
		appPref.SaveData("first", "logged");
		Intent i = new Intent(getActivity(), HomeActivity.class);
		startActivity(i);

	}

	private void NormalLogin(String response) {
		// TODO Auto-generated method stub

		try {

			System.out.println("responseresponse" + response);
			LoginBackend detail = (new Gson()).fromJson(response,
					LoginBackend.class);

			applicant appin = detail.applicant;
			log("my id is " + appin.id);
			log("my id is " + detail.applicant.id);

			String appid = String.valueOf(detail.applicant.id);
			System.out.println("my id is " + appid);

			appPref.SaveData("norm_log", "true");
			appPref.SaveData("Uid", appid);
			appPref.SaveData("email", detail.applicant.email);

			if (detail.applicant.payment) {
				appPref.SaveIntData("payed", 200);
			}

			if (detail.applicant.profile_id != null) {
				appPref.SaveData("prof_id", detail.applicant.profile_id);
			} else {
				appPref.SaveData("prof_id", "");
			}

			// App logs in
			getActivity().finish();
			appPref.SaveData("first", "logged");
			Intent i = new Intent(getActivity(), HomeActivity.class);
			startActivity(i);
			getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void error(String response, int value) {
		// TODO Auto-generated method stub

		try {
			if (value == 5) {
				ForgotError detail = (new Gson()).fromJson(response.toString(),
						ForgotError.class);

				if (detail.passwords != null) {
					toast(detail.passwords.get(0));
				}
			} else {
				if (response != null) {
					ErrorApi detail = (new Gson()).fromJson(
							response.toString(), ErrorApi.class);

					if (detail.message != null) {
						toast(detail.message);
					} else if (detail.error != null) {
						toast(detail.error);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("error " + response);

	}

	@OnClick(R.id.create_aacnt)
	public void SignInButton(View v) {

		ed_username.setText("");
		ed_password.setText("");
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		Intent i = new Intent(getActivity(), Registration.class);
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

		// animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		// if (ed_username.getText().toString().equals("")) {
		// ed_username.setError("Please enter username");
		// } else if (ed_password.getText().toString().equals("")) {
		// ed_password.setError("Please enter password");
		// } else {
		//
		// if (con.isConnectingToInternet()) {
		// signin(ed_username.getText().toString(), ed_password.getText()
		// .toString());
		// } else {
		// toast("No net connection");
		// }
		//
		// }

	}

	@OnClick(R.id.login_aacnt)
	public void CreateAccount(View v) {

		// Intent i = new Intent(getActivity(), Registration.class);
		// startActivity(i);
		// getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		if (ed_username.getText().toString().equals("")) {
			ed_username.setError("Please enter registered email");
		} else if (ed_password.getText().toString().equals("")) {
			ed_password.setError("Please enter password");
		} else {

			if (con.isConnectingToInternet()) {
				signin(ed_username.getText().toString(), ed_password.getText()
						.toString());
			} else {
				toast("No net connection");
			}

		}

	}

	// -------------------------facebook part----------------------------
	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// Respond to session state changes, ex: updating the view
			onClickLogin();
		}
	}

	private void onClickLogin() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setPermissions(
					Arrays.asList("public_profile"))
					.setCallback(statusCallback));
		} else {
			Session.openActiveSession(getActivity(), this, true, statusCallback);
		}
	}

	boolean fb_login;

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (state.isOpened()) {
			// loc(session);
			Request meRequest = Request.newMeRequest(session,
					new GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							if (response.getError() == null && !fb_login) {
								System.out.println("responseresponse"
										+ response);
								// System.out.println("response"
								// + user.getLocation().getName());
								String token = session.getAccessToken();

								fb_login = true;
								String email = user.getProperty("email")
										.toString();
								String urls = "https://graph.facebook.com/"
										+ user.getId() + "/picture?type=large";
								Log.v("Log", "urls : " + urls);
								Log.v("Log", "Response : " + response);
								Log.v("Log", "UserID : " + user.getId());
								Log.v("Log", "facebook birthday  : " + user.getBirthday());
								
//								System.out.println("user birthday facebook "+user.getBirthday());
								Log.v("Log",
										"User FirstName : "
												+ user.getFirstName());
								Log.v("Log", "User email : " + email);
								// String url = "fblogin.php?facebook_id="
								// + user.getId() + "&email=" + email;
								// appPrefes.SaveData("fbemail", email);
								// appPrefes.SaveData("fbname",
								// user.getFirstName());
								// appPrefes.SaveData("fbid", user.getId());
								// okhttp(url, 3, true);
								// appPreferences.setFBUserName(user.getName());
								appPref.SaveData("sociallastname_fb", user.getLastName());
								appPref.SaveData("socialname_fb",
										user.getFirstName());
								appPref.SaveData("socialemail_fb", email);
								appPref.SaveData("socialid_fb", user.getId());
								callFacebookLink(user.getFirstName(), urls,
										user.getId(), email);

							}
						}

					});

			// Execute the request
			meRequest.executeAsync();
			Log.i("Log", "Logged in...");
		} else if (state.isClosed()) {
			fb_login = false;
			Log.i("Log", "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void fblogin(String response, boolean success) {
		// TODO Auto-generated method stub
		System.out.println("Login.parseresult()" + response + success);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}

		Session.setActiveSession(null);
		if (success) {
			// afterlogin(response, success);
		} else {
			// nextfragment(new FbLogin(), true);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		LISessionManager.getInstance(getActivity()).onActivityResult(
				getActivity(), requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (requestCode != Activity.RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		signOutFromGplus();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// ----------------------------G+
	// login-----------------------------------------------

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
					getActivity(), 0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mSignInClicked = false;
		Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG)
				.show();
		// getActivity().finish();
		// appPref.SaveData("first", "logged");
		// Intent i = new Intent(getActivity(), HomeActivity.class);
		// startActivity(i);

		getProfileInformation();

		// getProfileInformation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(getActivity(),
						RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	@OnClick(R.id.btn_sign_in)
	public void btn_sign_in() {

		if (mConnectionResult != null) {
			signInWithGplus();
		}

	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				Log.e(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);
				
				System.out.println("birthday "+currentPerson.getBirthday());

				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				appPref.SaveData("socialname_goog", personName);
				appPref.SaveData("sociallastname_goog", "");
				appPref.SaveData("socialemail_goog", email);
				appPref.SaveData("socialid_goog", currentPerson.getId());
				

				callGooglePlusLink(personName, personPhotoUrl,
						currentPerson.getId(), email);

			} else {
				Toast.makeText(getActivity(), "Person information is null",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callGooglePlusLink(String personName, String personPhotoUrl,
			String id, String email) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Google+");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[linkedIn_image]", personPhotoUrl);
		asynkhttp(params, 3, StaticClass.headlink
				+ "/v2/sessions/add_providers", true);

	}

	public void callLinkedPlusLink(String personName, String personPhotoUrl,
			String id, String email) {
		// TODO Auto-generated method stub

		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Linkedin");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[google_image]", personPhotoUrl);
		asynkhttp(params, 4, StaticClass.headlink
				+ "/v2/sessions/add_providers", true);

	}

	private void callFacebookLink(String personName, String personPhotoUrl,
			String id, String email) {

		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Facebook");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[facebook_image]", personPhotoUrl);
		asynkhttp(params, 2, StaticClass.headlink
				+ "/v2/sessions/add_providers", true);

	}

	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();

		}
	}

	// Social media login clicks

	@OnClick(R.id.facebook_lays)
	public void facebookClick(View v) {
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		authButton.performClick();
	}

	@OnClick(R.id.google_plus)
	public void GooglePlusClick(View v) {
		// btnSignIn.performClick();
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		if (mConnectionResult != null) {
			signInWithGplus();
		}
	}

	@OnClick(R.id.linked_lay)
	public void LinkedInClick(View v) {
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		((Tutorials) getActivity()).SignInLinkedIn();
	}

	// The dialog which shows to send forgot password confirmation email to user
	@OnClick(R.id.forgot_email)
	public void infoclick1() {
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.forgot_password);

		Button submit = (Button) dialog
				.findViewById(R.id.forgot_password_submit);
		final EditText emailForgot = (EditText) dialog
				.findViewById(R.id.ed_forgot_password);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Validate(emailForgot)) {
					dialog.dismiss();
					callForgotPassword(emailForgot.getText().toString());
				}

			}

		});

		dialog.show();

	}

	// forgot password API call
	private void callForgotPassword(String email) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.put("user[email]", email);

		String url = StaticClass.headlink + "/applicants/password";

		asynkhttp(params, 5, url, true);

	}

	// email checking function
	public boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	public boolean Validate(EditText emailForgot) {
		if (!isValidEmail(emailForgot.getText().toString())) {
			emailForgot.setError("Please enter a valid email");
			return false;
		} else if (emailForgot.getText().toString().equals("")) {
			emailForgot.setError("Please enter a email");
			return false;
		} else {
			return true;
		}

	}

}
