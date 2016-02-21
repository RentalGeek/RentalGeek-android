package com.rentalgeek.android.ui.fragment;

import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.loopj.android.http.RequestParams;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.FacebookLogin;
import com.rentalgeek.android.api.GoogleLogin;
import com.rentalgeek.android.api.LinkedInLogin;
import com.rentalgeek.android.api.LoginInterface;
import com.rentalgeek.android.api.RentalGeekLogin;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.FacebookLoginEvent;
import com.rentalgeek.android.bus.events.GoogleErrorEvent;
import com.rentalgeek.android.bus.events.GoogleLoginEvent;
import com.rentalgeek.android.bus.events.GoogleResolutionEvent;
import com.rentalgeek.android.bus.events.HideProgressEvent;
import com.rentalgeek.android.bus.events.LinkedInLoginEvent;
import com.rentalgeek.android.bus.events.RentalGeekLoginEvent;
import com.rentalgeek.android.bus.events.ShowProgressEvent;
import com.rentalgeek.android.bus.events.ShowRegistrationEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.mvp.login.LoginPresenter;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.activity.LoginResult;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentSignIn extends GeekBaseFragment {

    private static final String TAG = FragmentSignIn.class.getSimpleName();

    private LoginInterface login;
    private YoYo.YoYoString btnAnimation;
    private LoginPresenter presenter;

    @InjectView(R.id.username)
    @Required(order = 1, messageResId = R.string.email_required)
    @Email(order = 2,messageResId = R.string.email_format )
    EditText username_edittext;

    @InjectView(R.id.password)
    @Required(order = 3, messageResId = R.string.password_required)
    @Password(order = 4)
    @TextRule(order = 5, minLength = 8, messageResId = R.string.password_min_length)
    EditText password_edittext;

    @InjectView(R.id.create_account) TextView create_account_textview;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //Unfortunately called since we are using facebook sdk button in layout
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        presenter = new LoginPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.inject(this, view);
        username_edittext.setText(AppPreferences.getUserName());
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.splash_bg);
        return view;
    }

    public static FragmentSignIn newInstance() {
        return new FragmentSignIn();
    }

    @Override
    public void onStart() {
        super.onStart();

        if( login != null ){
            login.onStart(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        registerBus();//Silly hack incase you authenticate using a social login activity such as LinkedIn

        if( login != null ) {
            login.onStop(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        unregisterBus();//Silly hack incase you authenticate using a social login activity such as LinkedIn
        super.onDestroy();
    }

    @OnClick(R.id.google_plus)
    public void onGoogleLoginClick(View btn){
        btnAnimation = YoYo.with(Techniques.Flash).duration(1000).playOn(btn);
        login = new GoogleLogin();
        login.setup(getActivity());
        login.clicked(getActivity());
    }

    @OnClick(R.id.linkedin)
    public void onLinkedInLoginClick(View btn) {
        btnAnimation = YoYo.with(Techniques.Flash).duration(1000).playOn(btn);
        login = new LinkedInLogin();
        login.setup(getActivity());
        login.clicked(getActivity());
    }

    @OnClick(R.id.facebook)
    public void onFacebookLoginClick(View btn) {
        btnAnimation = YoYo.with(Techniques.Flash).duration(1000).playOn(btn);
        login = new FacebookLogin();
        login.setup(getActivity());
        login.clicked(getActivity());
    }

    @OnClick(R.id.rentalgeek)
    public void onRentalGeekLoginClick(View btn) {
        btnAnimation = YoYo.with(Techniques.Flash).duration(1000).playOn(btn);
        login = new RentalGeekLogin();
        login.setup(getActivity());
        login.setValidation(this);
        login.clicked(getActivity());
    }

    @OnClick(R.id.create_account)
    public void onCreateAccountClick() {
        AppEventBus.post(new ShowRegistrationEvent());
    }

    @OnClick(R.id.forgot_email)
    public void infoclick1() {
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgot_password);

        Button submit = (Button) dialog
            .findViewById(R.id.forgot_password_submit);
        final EditText emailForgot = (EditText) dialog
            .findViewById(R.id.ed_forgot_password);

        submit.setOnClickListener(new View.OnClickListener() {

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

    public void onEventMainThread(LoginResult event){
        super.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
        login.onActivityResult(getActivity(), event.getRequestCode(), event.getResultCode(), event.getData());
    }

    public void onEventMainThread(GoogleResolutionEvent event) {
        if( event.getConnectionResult() != null ) {
            ConnectionResult result = event.getConnectionResult();

            try {
                result.startResolutionForResult(getActivity(),0);
            }

            catch(IntentSender.SendIntentException e) {
                AppLogger.log(TAG,e);
            }
        }
    }

    public void onEventMainThread(GoogleLoginEvent event) {
        if( event.getBundle() != null ) {
            Bundle bundle = event.getBundle();

            String fullname = bundle.getString(Constants.FULLNAME);
            String photoUrl = bundle.getString(Constants.PHOTO_URL);
            String id = bundle.getString(Constants.ID);
            String email = bundle.getString(Constants.EMAIL);

            presenter.googleLogin(fullname, photoUrl, id, email);
        }
    }

    public void onEventMainThread(LinkedInLoginEvent event) {
        if( event.getBundle() != null ) {
            Bundle bundle = event.getBundle();

            String fullname = bundle.getString(Constants.FULLNAME);
            String id = bundle.getString(Constants.ID);
            String email = bundle.getString(Constants.EMAIL);

            presenter.linkedinLogin(fullname, id, email);
        }
    }

    public void onEventMainThread(FacebookLoginEvent event) {
        if( event.getBundle() != null ) {
            Bundle bundle = event.getBundle();

            String fullname = bundle.getString(Constants.FULLNAME);
            String email = bundle.getString(Constants.EMAIL);

            presenter.facebookLogin(fullname, email);
        }
    }

    public void onEventMainThread(RentalGeekLoginEvent event) {
        String email = username_edittext.getText().toString();
        String password = password_edittext.getText().toString();

        presenter.rentalgeekLogin(email, password);
    }

    public void onEventMainThread(ShowProgressEvent event) {
        showProgressDialog(event.getMsgId());
    }

    public void onEventMainThread(HideProgressEvent event) {
        hideProgressDialog();
    }

    public void onEventMainThread(GoogleErrorEvent event) {
        if( event.getConnectionResult() != null  ) {
            ConnectionResult result = event.getConnectionResult();
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(), 0);
        }
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

    private void ForgotMailSentParse(String response) {
        DialogManager.showCrouton(activity, "Verification email sent to your mail please check");
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
