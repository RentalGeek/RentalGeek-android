package com.rentalgeek.android.mvp.login;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.bus.events.HideProgressEvent;
import com.rentalgeek.android.bus.events.ShowHomeEvent;
import com.rentalgeek.android.bus.events.ShowProgressEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;
import com.rentalgeek.android.utils.ObscuredSharedPreferences;

public class LoginPresenter implements Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private FragmentSignIn view;

    public LoginPresenter(FragmentSignIn view) {
        this.view = view;
    }

    @Override
    public void googleLogin(String fullname, String photoUrl, String id, String email) {

        RequestParams params = new RequestParams();
        params.put("provider[uid]", id);
        params.put("provider[provider]", "Google+");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[google_image]", photoUrl);

        socialLogin(params);
    }

    @Override
    public void linkedinLogin(String fullname, String id, String email) {
        RequestParams params = new RequestParams();
        params.put("provider[uid]", id);
        params.put("provider[provider]", "Linkedin");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[linkedIn_image]", "");

        socialLogin(params);
    }

    @Override
    public void facebookLogin(String fullname, String email) {
        RequestParams params = new RequestParams();
        params.put("provider[uid]","");
        params.put("provider[provider]", "Facebook");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[facebook_image]", "");

        socialLogin(params);
    }

    @Override
    public void rentalgeekLogin(String email, String password) {
        AppPreferences.setUserName(email);
        geekLogin(email, password);
    }

    private void socialLogin(RequestParams params){
        GlobalFunctions.postApiCall(null, ApiManager.getAddProvider(""),
                params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        AppEventBus.post(new ShowProgressEvent(R.string.dialog_msg_loading));
                    }

                    @Override
                    public void onFinish() {
                        AppEventBus.post(new HideProgressEvent());
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            parseResponse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String error) {
                        AppLogger.log(TAG,ex);
                        String title = RentalGeekApplication.getResourceString(R.string.login_title);
                        String message = RentalGeekApplication.getResourceString(R.string.oops);
                        AppEventBus.post(new ErrorAlertEvent(title, message));
                    }
                });
    }

    private void geekLogin(final String email, final String password){
        final RequestParams params = new RequestParams();
        params.put("user[email]", email);
        params.put("user[password]", password);

        GlobalFunctions.postApiCall(null, ApiManager.getSignin(),
                params, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        AppEventBus.post(new ShowProgressEvent(R.string.dialog_msg_loading));
                    }

                    @Override
                    public void onFinish() {
                        AppEventBus.post(new HideProgressEvent());
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            ObscuredSharedPreferences prefs = new ObscuredSharedPreferences(view.getActivity(), view.getActivity().getSharedPreferences("com.android.rentalgeek", Context.MODE_PRIVATE));
                            prefs.edit().putString(ObscuredSharedPreferences.USERNAME_PREF, email).commit();
                            prefs.edit().putString(ObscuredSharedPreferences.PASSWORD_PREF, password).commit();
                            parseResponse(content);
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String error) {
                        AppLogger.log(TAG, ex);
                        String title = RentalGeekApplication.getResourceString(R.string.login_title);
                        String message = RentalGeekApplication.getResourceString(R.string.invalid_login);
                        AppEventBus.post(new ErrorAlertEvent(title,message));
                    }
                });
    }

    private void parseResponse(String response) {
        System.out.println(response);
        LoginBackend detail = GeekGson.getInstance().fromJson(response,LoginBackend.class);
        SessionManager.Instance.onUserLoggedIn(detail);
        AppEventBus.post(new ShowHomeEvent());
    }

}
