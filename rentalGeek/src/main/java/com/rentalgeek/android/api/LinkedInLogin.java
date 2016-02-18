package com.rentalgeek.android.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.AccessToken;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ErrorCroutonEvent;
import com.rentalgeek.android.bus.events.LinkedInLoginEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkedInLogin implements LoginInterface {

    private static final String TAG = LinkedInLogin.class.getSimpleName();

    private LISessionManager sessionManager;
    private APIHelper apiHelper;

    private void getProfileRequest(Activity context) {
        String url = ApiManager.getLinkedInUrl();
        apiHelper.getRequest(context, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                try {
                    JSONObject responseObject = apiResponse.getResponseDataAsJson();
                    String firstName = responseObject.getString("firstName");
                    String id = responseObject.getString("id");
                    String email = responseObject.getString("emailAddress");
                    System.out.println(String.format("First name: %s\nid: %s\nemail %s", firstName, id, email));

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FULLNAME,firstName);
                    bundle.putString(Constants.ID,id);
                    bundle.putString(Constants.EMAIL,email);

                    AppEventBus.post(new LinkedInLoginEvent(bundle));
                }

                catch (JSONException e) {
                    AppLogger.log(TAG,e);
                }
            }

            @Override
            public void onApiError(LIApiError LIApiError) {

            }
        });
    }
    @Override
    public void clicked(final Activity context) {
        System.out.println("LinkedIn clicked");

        LISession session = sessionManager.getSession();
        AccessToken token = sessionManager.getSession().getAccessToken();

        if( session != null && session.isValid() ) {
            token = sessionManager.getSession().getAccessToken();
        }

        if( token != null ) {
            System.out.println("Valid LinkedIn token found");
            sessionManager.init(token);
            getProfileRequest(context);
        }

        else {
            if ( context != null ) {
                System.out.println("No valid LinkedIn token found");

                sessionManager.init(context, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        System.out.println("LinkedIn Authentication success");
                        getProfileRequest(context);
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        System.out.println("LinkedIn Authentication error");
                        String message = RentalGeekApplication.getResourceString(R.string.linkedin_auth_error);
                        AppEventBus.post(new ErrorCroutonEvent(message));
                    }
                },true);
            }
        }

    }

    @Override
    public void setup(Activity context) {
        sessionManager = LISessionManager.getInstance(context);
        apiHelper = APIHelper.getInstance(context);
    }

    private Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE,Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }

    @Override
    public void onStart(Activity context) {
        System.out.println("LinkedIn onStart");
    }

    @Override
    public void onStop(Activity context) {
        System.out.println("LinkedIn onStop");
        if( apiHelper != null ) {
            if( context != null ) {
                apiHelper.cancelCalls(context);
            }
        }
    }

    @Override
    public void onActivityResult(Activity context,int requestCode, int resultCode,Intent data) {
        System.out.println("LinkedIn onActivityResult");
        if( context != null ) {
            sessionManager.onActivityResult(context,requestCode,resultCode,data);
        }
    }

    @Override
    public void setValidation(Object controller) {

    }

}
