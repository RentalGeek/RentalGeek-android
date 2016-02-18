package com.rentalgeek.android.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.FacebookLoginEvent;
import com.rentalgeek.android.utils.Constants;

import org.json.JSONObject;

import java.util.Arrays;

public class FacebookLogin implements LoginInterface {

    public static final String TAG = FacebookLogin.class.getSimpleName();

    private LoginButton loginBtn;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken token;

    @Override
    public void clicked(Activity context) {
        System.out.println("Facebook clicked");
        loginBtn.performClick();
    }

    @Override
    public void setup(Activity context) {
        loginBtn = (LoginButton) context.findViewById(R.id.facebook_login_btn);
        loginBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if( currentAccessToken != null && ! currentAccessToken.isExpired() ) {
                    System.out.println("Valid Facebook token found");
                    token = currentAccessToken;
                }

                else {
                    System.out.println("No valid Facebook token found");
                }
            }
        };

        callbackManager = CallbackManager.Factory.create();

        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("Facebook authentication success");
                token = loginResult.getAccessToken();
                getProfile();
            }

            @Override
            public void onCancel() {
                System.out.println("Facebook authentication cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("Facebook authentication error");
            }
        });
    }

    private void getProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        String data = response.toString();
                        String name = object.optString("name");
                        String email = object.optString("email");

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.FULLNAME,name);
                        bundle.putString(Constants.EMAIL,email);

                        AppEventBus.post(new FacebookLoginEvent(bundle));
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart(Activity context) {
        System.out.println("Facebook onStart");
        accessTokenTracker.startTracking();
    }

    @Override
    public void onStop(Activity context) {
        System.out.println("Facebook onStop");
        LoginManager.getInstance().logOut();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        System.out.println("Facebook onActivityResult");
        if( callbackManager != null ) {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public void setValidation(Object controller) {

    }
}
