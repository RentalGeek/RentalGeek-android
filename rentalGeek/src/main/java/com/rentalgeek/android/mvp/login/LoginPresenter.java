package com.rentalgeek.android.mvp.login;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.HideProgressEvent;
import com.rentalgeek.android.bus.events.ShowHomeEvent;
import com.rentalgeek.android.bus.events.ShowProgressEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.GeekGson;

/**
 * Created by Alan R on 10/21/15.
 */
public class LoginPresenter implements Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    @Override
    public void googleLogin(String fullname, String photoUrl, String id, String email) {

        RequestParams params = new RequestParams();
        params.put("provider[uid]", id);
        params.put("provider[provider]", "Google+");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[google_image]", photoUrl);

        login(params);
    }

    @Override
    public void linkedinLogin(String fullname, String id, String email) {
        RequestParams params = new RequestParams();
        params.put("provider[uid]", id);
        params.put("provider[provider]", "Linkedin");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[linkedIn_image]", "");

        login(params);
    }

    @Override
    public void facebookLogin(String fullname, String email) {
        RequestParams params = new RequestParams();
        params.put("provider[uid]","");
        params.put("provider[provider]", "Facebook");
        params.put("provider[email]", email);
        params.put("provider[name]", fullname);
        params.put("provider[facebook_image]", "");

        login(params);
    }

    private void login(RequestParams params){
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
                });
    }

    private void parseResponse(String response) {
        LoginBackend detail = GeekGson.getInstance().fromJson(response,LoginBackend.class);
        SessionManager.Instance.onUserLoggedIn(detail);
        AppEventBus.post(new ShowHomeEvent());
    }
}
