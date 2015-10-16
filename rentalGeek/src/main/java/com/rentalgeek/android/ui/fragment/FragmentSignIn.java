package com.rentalgeek.android.ui.fragment;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.BuildConfig;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorApi;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.system.AppSystem;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityRegistration;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.OkAlert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentSignIn extends GeekBaseFragment implements ConnectionCallbacks, OnConnectionFailedListener {

    public static final int LI_SIGN_IN = 0;
    public static final int GP_SIGN_IN = 2;

    private static final String TAG = "FragmentSignIn";

    private YoYo.YoYoString animation_obj;

    private static final int PROFILE_PIC_SIZE = 400;

    private GoogleApiClient mGoogleApiClient;
    
    private AppPrefes appPref;

    private boolean mIntentInProgress;

    private boolean clickedGoogle = false;
    private boolean clickedFacebook = false;
    private boolean clickedLinkedIn = false;

    private boolean mResolveOnFail = false;
    
    private ConnectionResult mConnectionResult;
    
    private CallbackManager facebookCallbackManager;

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

    @InjectView(R.id.layoutThirdPartyLogins)
    LinearLayout layoutThirdPartyLogins;

    @InjectView(R.id.ed_username)
    EditText ed_username;

    @InjectView(R.id.fbLoginButton)
    LoginButton fbLoginButton;

    @InjectView(R.id.ed_password)
    EditText ed_password;

    public static FragmentSignIn newInstance() {
        FragmentSignIn fragment = new FragmentSignIn();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        facebookCallbackManager = CallbackManager.Factory.create();

        if( mGoogleApiClient == null ) 
            mGoogleApiClient = buildGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        ButterKnife.inject(this, v);

        setUpdateState();

        // facebook essentials
        fbLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        fbLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AppLogger.log(TAG, "fb success:"+loginResult.getAccessToken());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                String data = response.toString();
                                AppLogger.log(TAG, data);
                                String name = object.optString("name");
                                String email = object.optString("email");
                                callFacebookLink(name, "", "", email);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                DialogManager.showCrouton(activity, "Facebook Login Cancelled");
                clickedFacebook = false;
            }

            @Override
            public void onError(FacebookException exception) {
                AppLogger.log(TAG, exception);
                DialogManager.showCrouton(activity, exception.getMessage());
                clickedFacebook = false;
            }
        });


        if (AppSystem.Instance.isDebugBuild(activity)) {
            ed_username.setText(AppPreferences.getUserName());
            ed_password.setText(AppPreferences.getPassword());
        }

        layoutThirdPartyLogins.setVisibility(AppSystem.isV1Build ? View.GONE : View.VISIBLE);

        return v;
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new com.google.android.gms.common.api.Scope(Scopes.PLUS_LOGIN))
                .addScope(new com.google.android.gms.common.api.Scope(Scopes.PLUS_ME));
        System.out.println("Build Google API Client");
        return builder.build();
    }

    private void signin(String a, String b) {

        RequestParams params = new RequestParams();
        params.put("user[email]", a);
        params.put("user[password]", b);

        GlobalFunctions.postApiCall(getActivity(), ApiManager.getSignin(),
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
                        NormalLogin(content);
                    } catch (Exception e) {
                        AppLogger.log(TAG, e);
                    }
                }

                @Override
                public void onAuthenticationFailed() {

                }

                @Override
                public void onFailure(Throwable ex, String failureResponse) {
                    super.onFailure(ex, failureResponse);
                    ErrorApi error = (new Gson()).fromJson(failureResponse, ErrorApi.class);
                    if (error != null) {
                        if (!error.success) {
                            String message = error.message;
                            if (!TextUtils.isEmpty(message)) {
                                String title = getResources().getString(R.string.login_title);
                                String msg = getResources().getString(R.string.invalid_login);
                                OkAlert.show(getActivity(), title, msg);
                                return;
                            }
                        }
                    }

                    OkAlert.showUnknownError(getActivity());
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void ForgotMailSentParse(String response) {
        DialogManager.showCrouton(activity, "Verification email sent to your mail please check");
    }

    private void LinkedInParse(String response) {
        LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);
        SessionManager.Instance.onUserLoggedIn(detail);
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }

    private void googlePlusParse(String response) {
        LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);
        signOutFromGplus();
        SessionManager.Instance.onUserLoggedIn(detail);
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }

    private void FaceBookLogin(String response) {
        LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);
        LoginManager.getInstance().logOut();
        SessionManager.Instance.onUserLoggedIn(detail);
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }

    private void NormalLogin(String response) {
        if (BuildConfig.DEBUG) {
            AppPreferences.setUserName(ed_username.getText().toString());
            AppPreferences.setPassword(ed_password.getText().toString());
        }
        LoginBackend detail = (new Gson()).fromJson(response, LoginBackend.class);
        SessionManager.Instance.onUserLoggedIn(detail);
        Navigation.navigateActivity(activity, ActivityHome.class, true);
    }

    @OnClick(R.id.create_aacnt)
    public void SignInButton(View v) {
        ed_username.setText("");
        ed_password.setText("");
        animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);

        Navigation.navigateActivity(activity, ActivityRegistration.class, false);
    }

    @OnClick(R.id.login_aacnt)
    public void CreateAccount(View v) {
        animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
        if (ed_username.getText().toString().equals("")) {
            ed_username.setError("Please enter registered email");
        } else if (ed_password.getText().toString().equals("")) {
            ed_password.setError("Please enter password");
        } else {
            signin(ed_username.getText().toString(), ed_password.getText().toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        super.onActivityResult(requestCode,resultCode,data);

        if( resultCode == getActivity().RESULT_OK ) {
        
            if( clickedGoogle ) {
                mResolveOnFail = false; 
                mGoogleApiClient.connect();
            }

            else if( clickedFacebook ) {
                facebookCallbackManager.onActivityResult(requestCode,resultCode,data);
            }

            else if ( clickedLinkedIn ) {
                LISessionManager.getInstance(activity.getApplicationContext()).onActivityResult(activity, requestCode, resultCode, data);
            }
        }                    
    }

    private static final String SAVED_PROGRESS = "sign_in_progress";

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        
        if( result.hasResolution() ) {
            System.out.println("Resolution exists");

            mConnectionResult = result;

            if( mResolveOnFail ) {
                startGoogleResolution();    
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        System.out.println("Connected to Google+");

        if( clickedGoogle ) {
            getProfileInformation();
            clickedGoogle = false;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.

        if( cause == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED ) {
            System.out.println("G+ API client service disconnected");
        }

        else if ( cause == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            System.out.println("Network lost while connecting to G+ API client service");
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if ( mGoogleApiClient != null && ( mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting() ) ) {
            mGoogleApiClient.disconnect();
            System.out.println("Disconnecting G+ API client");
        }
    }

    private void startGoogleResolution() {
        if( mConnectionResult != null ) {
            try {
                mConnectionResult.startResolutionForResult(getActivity(),GP_SIGN_IN);
                mConnectionResult = null;
            }

            catch(SendIntentException e){
                System.out.println("Reconnecting");
                mGoogleApiClient.connect();
            }
        }
    }

    private Dialog createErrorDialog() {
        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    getActivity(),
                    GP_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e(TAG, "Google Play services resolution cancelled");
                            mSignInProgress = STATE_DEFAULT;
                            // mStatus.setText(R.string.status_signed_out);
                        }
                    });
        } else {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Play Services Error")
                    .setPositiveButton("Close",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError);
                                    mSignInProgress = STATE_DEFAULT;
                                    //mStatus.setText(R.string.status_signed_out);
                                }
                            }).create();
        }
    }
    /**
     * Sign-in into google
     * */
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;

    // Used to determine if we should ask for a server auth code when connecting the
    // GoogleApiClient.  False by default so that this sample can be used without configuring
    // a WEB_CLIENT_ID and SERVER_BASE_URL.
    private boolean mRequestServerAuthCode = false;

    // Client id 433959508661-2mepmf7qms9pdih67ea5o91sa5fpcjb8.apps.googleusercontent.com

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private void getProfileInformation() {
        try {
            mSignInProgress = STATE_DEFAULT;
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

                callGooglePlusLink(personName, personPhotoUrl, currentPerson.getId(), email);

            } else {
                DialogManager.showCrouton(activity, "G+ Info not available");
                System.out.println("G+ Info not available");
            }
        } catch (Exception e) {
            AppLogger.log(TAG, e);
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
                            googlePlusParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }


                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                        ErrorApi error = (new Gson()).fromJson(failureResponse, ErrorApi.class);
                        if (error != null) {
                            if (!error.success) {
                                String message = error.message;
                                if (!TextUtils.isEmpty(message)) {
                                    String title = getResources().getString(R.string.login_title);
                                    String msg = getResources().getString(R.string.invalid_login);
                                    OkAlert.show(getActivity(), title, msg);
                                    return;
                                }
                            }
                        }

                        OkAlert.showUnknownError(getActivity());
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }

    public void callLinkedPlusLink(String personName, String personPhotoUrl, String id, String email) {
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
                            LinkedInParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }


                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                        ErrorApi error = (new Gson()).fromJson(failureResponse, ErrorApi.class);
                        if (error != null) {
                            if (!error.success) {
                                String message = error.message;
                                if (!TextUtils.isEmpty(message)) {
                                    String title = getResources().getString(R.string.login_title);
                                    String msg = getResources().getString(R.string.invalid_login);
                                    OkAlert.show(getActivity(), title, msg);
                                    return;
                                }
                            }
                        }

                        OkAlert.showUnknownError(getActivity());
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });

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
                            FaceBookLogin(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }


                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                        ErrorApi error = (new Gson()).fromJson(failureResponse, ErrorApi.class);
                        if (error != null) {
                            if (!error.success) {
                                String message = error.message;
                                if (!TextUtils.isEmpty(message)) {
                                    String title = getResources().getString(R.string.login_title);
                                    String msg = getResources().getString(R.string.invalid_login);
                                    OkAlert.show(getActivity(), title, msg);
                                    return;
                                }
                            }
                        }

                        OkAlert.showUnknownError(getActivity());
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });

    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // Social media login clicks

    @OnClick(R.id.facebook_lays)
    public void facebookClick(View v) {
        animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
        fbLoginButton.performClick();
        clickedFacebook = true;
        System.out.println("Clicked Facebook login button");
    }

    @OnClick(R.id.google_plus)
    public void GooglePlusClick(View v) {
        animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);

        clickedGoogle = true;
        
        if( mConnectionResult != null && mConnectionResult.hasResolution() ) {
            startGoogleResolution();
            System.out.println("Starting Google Resolution");
        }

        else {

            if( mGoogleApiClient.isConnected() ) {
                mResolveOnFail = false;
                getProfileInformation();
            }

            else {
                mGoogleApiClient.connect();
            }
        }
    }

    @OnClick(R.id.linked_lay)
    public void LinkedInClick(View v) {
        animation_obj = YoYo.with(Techniques.Flash).duration(1000).playOn(v);
        clickedLinkedIn = true;
        SignInLinkedIn();
    }


    /***********************************************
     *  LinkedIn Login
     **********************************************/

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://"
            + host
            + "/v1/people/~:(first-name,last-name,picture-url,id,email-address)";
    private static final String shareUrl = "https://" + host
            + "/v1/people/~/shares";

    public void SignInLinkedIn() {
        LISessionManager.getInstance(activity.getApplicationContext()).init(
                activity, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        setUpdateState();

                        APIHelper apiHelper = APIHelper.getInstance(activity.getApplicationContext());
                        apiHelper.getRequest(activity, topCardUrl,
                                new ApiListener() {
                                    @Override
                                    public void onApiSuccess(ApiResponse s) {

                                        System.out.println("linked in response " + s.getResponseDataAsJson());

                                        try {
                                            callLinkedPlusLink(
                                                    s.getResponseDataAsJson().getString("firstName"),
                                                    "", s.getResponseDataAsJson().getString("id"),
                                                    s.getResponseDataAsJson().getString("emailAddress"));
                                        } catch (JSONException e) {
                                            AppLogger.log(TAG, e);
                                        }

                                    }

                                    @Override
                                    public void onApiError(LIApiError error) {
                                        AppLogger.log(TAG, error);
                                        DialogManager.showCrouton(activity, error.getMessage());
                                        clickedLinkedIn = false;
                                    }
                                });

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        setUpdateState();

                        Toast.makeText(activity.getApplicationContext(),
                                "failed " + error.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                }, true);
    }

    private com.linkedin.platform.utils.Scope buildScope() {
        return com.linkedin.platform.utils.Scope.build(com.linkedin.platform.utils.Scope.R_BASICPROFILE, com.linkedin.platform.utils.Scope.W_SHARE, com.linkedin.platform.utils.Scope.R_EMAILADDRESS);
    }

    private void setUpdateState() {
        LISessionManager sessionManager = LISessionManager.getInstance(activity.getApplicationContext());
        LISession session = sessionManager.getSession();
        boolean accessTokenValid = session.isValid();

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
                            ForgotMailSentParse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }

    // email checking function
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
