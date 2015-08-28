package com.rentalgeek.android.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.GoogleBackend;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.database.ProfileTable;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityRegistration;
import com.rentalgeek.android.ui.activity.ActivityTutorials;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ConnectionDetector;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentSignIn extends Fragment implements ConnectionCallbacks,
		OnConnectionFailedListener {

	CallbackManager callbackManager;

	public static final int RC_SIGN_IN = 0;
    public static final int FB_SIGN_IN = 1;

	private static final String TAG = "ActivityMain";
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

	@InjectView(R.id.fbLoginButton)
    LoginButton fbLoginButton;

//	private Session.StatusCallback statusCallback = new SessionStatusCallback();
//	private UiLifecycleHelper uiHelper;

	@InjectView(R.id.ed_password)
	EditText ed_password;

	//VolleyForAll volley;

	public static FragmentSignIn newInstance() {
		FragmentSignIn fragment = new FragmentSignIn();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

		appPref = new AppPrefes(getActivity(), "rentalgeek");
		con = new ConnectionDetector(getActivity());
		if (appPref.getData("first").equals("")) {

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.sigin_latest, container, false);
		// signin();
		ButterKnife.inject(this, v);
		create_aacnt.setText(Html.fromHtml("Not a member? <u>Create Account</u>"));

		// facebook essentials
        android.app.Fragment fragment = new NativeFragmentWrapper(this);
		//fbLoginButton.setFragment(this);
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
		fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException exception) {
				// App code
			}
		});

		// google plus initialization
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();

		return v;

	}

	private void signin(String a, String b) {

		RequestParams params = new RequestParams();
		params.put("user[email]", a);
		params.put("user[password]", b);

        GlobalFunctions.postApiCall(getActivity(), ApiManager.getSignin(),
                                    params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {

                    }

                    @Override
                    public void onFinish() {
                        //progresscancel();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            NormalLogin(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
		//asynkhttp(params, 1, ApiManager.getSignin(), AppPreferences.getAuthToken(), true);

	}

	// @OnClick(R.id.sign_button)
	// public void SignIn() {
	//
	// Intent i = new Intent(getActivity(), ActivityHome.class);
	// startActivity(i);
	// getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);
	//
	// }

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		ButterKnife.reset(this);
	}

/*	@Override
	public void parseresult(String response, boolean success, int value) {


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
	}*/

	private void ForgotMailSentParse(String response) {

        DialogManager.showCrouton(getActivity(), "Verification email sent to your mail please check");

	}

	private void LinkedInParse(String response) {

		GoogleBackend detail = (new Gson()).fromJson(response, GoogleBackend.class);

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
			profdets.uid = appPref.getData("Uid");
			profdets.firstname=appPref.getData("socialname_link");
			profdets.lastname=appPref.getData("sociallastname_link");
			profdets.save();
		}
		
		// App logs in
		getActivity().finish();
		appPref.SaveData("first", "logged");
		Intent i = new Intent(getActivity(), ActivityHome.class);
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

	}

	private void googlePlusParse(String response) {

		System.out.println("google response " + response);
		GoogleBackend detail = (new Gson()).fromJson(response, GoogleBackend.class);

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
		Intent i = new Intent(getActivity(), ActivityHome.class);
		startActivity(i);
		getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

	}

	private void FaceBookLogin(String response) {

		GoogleBackend detail = (new Gson()).fromJson(response, GoogleBackend.class);

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
		Intent i = new Intent(getActivity(), ActivityHome.class);
		startActivity(i);

	}

	private void NormalLogin(String response) {


		try {

			System.out.println("responseresponse" + response);
			LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);

			AppPreferences.setAuthToken(detail.user.authentication_token);

			ApiManager.currentUser = detail.user;
			//log("my id is " + ApiManager.currentUser.id);
			AppLogger.log(TAG, "my id is " + detail.user.id);

			String appid = String.valueOf(detail.user.id);
			System.out.println("my id is " + appid);

			appPref.SaveData("norm_log", "true");
			appPref.SaveData("Uid", appid);
			appPref.SaveData("email", detail.user.email);

			if (detail.user.payment) {
				appPref.SaveIntData("payed", 200);
			}

			if (detail.user.profile_id != null) {
				appPref.SaveData("prof_id", detail.user.profile_id);
			} else {
				appPref.SaveData("prof_id", "");
			}

			// App logs in
			getActivity().finish();
			appPref.SaveData("first", "logged");
			Intent i = new Intent(getActivity(), ActivityHome.class);
			startActivity(i);
			getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

		} catch (Exception e) {
            AppLogger.log(TAG, e);
		}
	}

/*	@Override
	public void error(String response, int value) {

        FragmentActivity activity = getActivity();

		try {
			if (value == 5) {
				ForgotError detail = (new Gson()).fromJson(response.toString(), ForgotError.class);

				if (detail.passwords != null) {
					DialogManager.showCrouton(activity, detail.passwords.get(0));
				}
			} else {
				if (response != null) {
					ErrorApi detail = (new Gson()).fromJson(response.toString(), ErrorApi.class);

					if (detail.message != null) {
                        DialogManager.showCrouton(activity, detail.message);
					} else if (detail.error != null) {
                        DialogManager.showCrouton(activity, detail.error);
					}
				}
			}
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}

		System.out.println("error " + response);

	}*/

	@OnClick(R.id.create_aacnt)
	public void SignInButton(View v) {

		ed_username.setText("");
		ed_password.setText("");
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		Intent i = new Intent(getActivity(), ActivityRegistration.class);
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

		// Intent i = new Intent(getActivity(), ActivityRegistration.class);
		// startActivity(i);
		// getActivity().overridePendingTransition(R.anim.one_, R.anim.two_);

		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		if (ed_username.getText().toString().equals("")) {
			ed_username.setError("Please enter registered email");
		} else if (ed_password.getText().toString().equals("")) {
			ed_password.setError("Please enter password");
		} else {

//			if (con.isConnectingToInternet()) {
//				signin(ed_username.getText().toString(), ed_password.getText().toString());
//			} else {
//				DialogManager.showCrouton(getActivity(), "No net connection");
//			}

            signin(ed_username.getText().toString(), ed_password.getText().toString());

		}

	}

	// -------------------------facebook part----------------------------
/*
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
*/

	@Override
	public void onResume() {
		super.onResume();
//		Session session = Session.getActiveSession();
//		if (session != null && (session.isOpened() || session.isClosed())) {
//			onSessionStateChange(session, session.getState(), null);
//		}
//		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		//uiHelper.onActivityResult(requestCode, resultCode, data);
		LISessionManager.getInstance(getActivity()).onActivityResult(getActivity(), requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (requestCode != Activity.RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else if (requestCode == FB_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

	}

	@Override
	public void onPause() {
		super.onPause();
		//uiHelper.onPause();
        signOutFromGplus();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//uiHelper.onSaveInstanceState(outState);
	}

	// ----------------------------G+
	// login-----------------------------------------------

	@Override
	public void onConnectionFailed(ConnectionResult result) {


		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0).show();
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

		mSignInClicked = false;
		Toast.makeText(getActivity(), "User is connected!", Toast.LENGTH_LONG)
				.show();
		// getActivity().finish();
		// appPref.SaveData("first", "logged");
		// Intent i = new Intent(getActivity(), ActivityHome.class);
		// startActivity(i);

		getProfileInformation();

		// getProfileInformation();
	}

	@Override
	public void onConnectionSuspended(int arg0) {

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
				mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
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
				Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
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
				

				callGooglePlusLink(personName, personPhotoUrl, currentPerson.getId(), email);

			} else {
				Toast.makeText(getActivity(), "Person information is null  ", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callGooglePlusLink(String personName, String personPhotoUrl, String id, String email) {
        
		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Google+");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[linkedIn_image]", personPhotoUrl);

		GlobalFunctions.postApiCall(getActivity(), ApiManager.getAddProvider(""),
				params, AppPreferences.getAuthToken(),
				new GeekHttpResponseHandler() {

					@Override
					public void onBeforeStart() {

					}

					@Override
					public void onFinish() {
						//progresscancel();
					}

					@Override
					public void onSuccess(String content) {
						try {
                            googlePlusParse(content);
						} catch (Exception e) {
							AppLogger.log(TAG, e);
						}
					}

					@Override
					public void onAuthenticationFailed() {

					}
				});
		//asynkhttp(params, 3, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

	}

	public void callLinkedPlusLink(String personName, String personPhotoUrl,
			String id, String email) {


		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Linkedin");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[google_image]", personPhotoUrl);

        GlobalFunctions.postApiCall(getActivity(), ApiManager.getAddProvider(""),
                params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {

                    }

                    @Override
                    public void onFinish() {
                        //progresscancel();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            LinkedInParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttp(params, 4, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

	}

	private void callFacebookLink(String personName, String personPhotoUrl, String id, String email) {

		RequestParams params = new RequestParams();
		params.put("provider[uid]", id);
		params.put("provider[provider]", "Facebook");
		params.put("provider[email]", email);
		params.put("provider[name]", personName);
		params.put("provider[facebook_image]", personPhotoUrl);

        GlobalFunctions.postApiCall(getActivity(), ApiManager.getAddProvider(""),
                params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {

                    }

                    @Override
                    public void onFinish() {
                        //progresscancel();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            FaceBookLogin(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttp(params, 2, ApiManager.getAddProvider(""), AppPreferences.getAuthToken(), true);

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
		fbLoginButton.performClick();
	}

	@OnClick(R.id.google_plus)
	public void GooglePlusClick(View v) {
		// btnSignIn.performClick();
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		if (mConnectionResult != null) {
			signInWithGplus();
		} else {
			Toast.makeText(getActivity(), "Can't connect to Google+  ", Toast.LENGTH_LONG).show();
		}
	}

	@OnClick(R.id.linked_lay)
	public void LinkedInClick(View v) {
		animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
		((ActivityTutorials) getActivity()).SignInLinkedIn();
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

		RequestParams params = new RequestParams();
		params.put("user[email]", email);

		String url = ApiManager.getApplicantPassword();

        GlobalFunctions.postApiCall(getActivity(), url,
                params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {

                    }

                    @Override
                    public void onFinish() {
                        //progresscancel();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            ForgotMailSentParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
        //asynkhttp(params, 5, url, AppPreferences.getAuthToken(), true);

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
