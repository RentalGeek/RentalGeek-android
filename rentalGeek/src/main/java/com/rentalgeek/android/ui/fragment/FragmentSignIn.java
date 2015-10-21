package com.rentalgeek.android.ui.fragment;

import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.GoogleLogin;
import com.rentalgeek.android.api.LinkedInLogin;
import com.rentalgeek.android.api.LoginInterface;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.GoogleErrorEvent;
import com.rentalgeek.android.bus.events.GoogleLoginEvent;
import com.rentalgeek.android.bus.events.GoogleResolutionEvent;
import com.rentalgeek.android.bus.events.HideProgressEvent;
import com.rentalgeek.android.bus.events.LinkedInLoginEvent;
import com.rentalgeek.android.bus.events.ShowProgressEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.mvp.login.LoginPresenter;
import com.rentalgeek.android.ui.activity.LoginResult;
import com.rentalgeek.android.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentSignIn extends GeekBaseFragment {

    private static final String TAG = FragmentSignIn.class.getSimpleName();

    private LoginInterface login;
    private YoYo.YoYoString btnAnimation;
    private LoginPresenter presenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        presenter = new LoginPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.inject(this, view);
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

    public void onEventMainThread(LoginResult event){
        super.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
        login.onActivityResult(getActivity(),event.getRequestCode(), event.getResultCode(), event.getData());
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

            presenter.googelLogin(fullname,photoUrl,id,email);
        }
    }

    public void onEventMainThread(LinkedInLoginEvent event) {
        if( event.getBundle() != null ) {
            Bundle bundle = event.getBundle();

            String fullname = bundle.getString(Constants.FULLNAME);
            String id = bundle.getString(Constants.ID);
            String email = bundle.getString(Constants.EMAIL);

            presenter.linkedinLogin(fullname,id,email);
        }
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
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),getActivity(),0);
        }
    }
}
