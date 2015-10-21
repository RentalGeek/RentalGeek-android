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
import com.rentalgeek.android.api.LoginInterface;
import com.rentalgeek.android.bus.events.GoogleErrorEvent;
import com.rentalgeek.android.bus.events.GoogleLoginEvent;
import com.rentalgeek.android.bus.events.GoogleResolutionEvent;
import com.rentalgeek.android.bus.events.HideProgressEvent;
import com.rentalgeek.android.bus.events.ShowProgressEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.mvp.login.LoginPresenter;
import com.rentalgeek.android.ui.activity.LoginResult;

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
            login.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if( login != null ) {
            login.onStop();
        }
    }

    @OnClick(R.id.google_plus)
    public void onGoogleLoginClick(View btn){
        btnAnimation = YoYo.with(Techniques.Flash).duration(1000).playOn(btn);
        login = new GoogleLogin();
        login.setup(getActivity());
        login.clicked();
    }

    public void onEventMainThread(LoginResult event){
        super.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
         login.onActivityResult(event.getRequestCode(), event.getResultCode());
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

            String fullname = bundle.getString(GoogleLogin.FULLNAME);
            String photoUrl = bundle.getString(GoogleLogin.PHOTO_URL);
            String id = bundle.getString(GoogleLogin.ID);
            String email = bundle.getString(GoogleLogin.EMAIL);

            presenter.googelLogin(fullname,photoUrl,id,email);
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
