package com.rentalgeek.android.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.GoogleErrorEvent;
import com.rentalgeek.android.bus.events.GoogleLoginEvent;
import com.rentalgeek.android.bus.events.GoogleResolutionEvent;
import com.rentalgeek.android.utils.Constants;

/**
 * Created by Alan R on 10/21/15.
 */
public class GoogleLogin implements LoginInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private SignInButton google_plus_btn;
    private boolean shouldResolve = false;
    private boolean isResolving = false;


    @Override
    public void clicked(Activity context) {
        System.out.println("Google+ clicked");
        if( googleApiClient != null && google_plus_btn != null ) {
            shouldResolve = true;
            googleApiClient.disconnect();
            googleApiClient.connect();
            google_plus_btn.performClick();
        }

        else
            shouldResolve = false;
    }

    @Override
    public void setup(Activity context) {
        System.out.println("Google+ setup");
        googleApiClient = new GoogleApiClient.Builder(context)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .addApi(Plus.API)
                            .addScope(new Scope(Scopes.PLUS_ME))
                            .addScope(new Scope(Scopes.EMAIL))
                            .build();

        google_plus_btn = (SignInButton)context.findViewById(R.id.google_plus_login_btn);

    }

    @Override
    public void onStart(Activity context) {
        System.out.println("Google+ onStart");
        if( googleApiClient != null ) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop(Activity context) {
        System.out.println("Google+ onStop");
        if( googleApiClient != null ) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(Activity context, int requestCode, int resultCode,Intent data) {
        System.out.println("Google+ onActivityResult");

        if( resultCode != Activity.RESULT_OK ) {
            shouldResolve = false;
        }

        isResolving = false;

        if( googleApiClient != null && ! googleApiClient.isConnecting() && ! googleApiClient.isConnected()) {
            googleApiClient.disconnect();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("Connected to Google+");
        shouldResolve = false;

        Person currentUser = Plus.PeopleApi.getCurrentPerson(googleApiClient);

        if ( currentUser != null) {
            System.out.println("Google+ account found");

            String fullname = currentUser.getDisplayName();
            String photoUrl = currentUser.getImage().getUrl();
            String profileUrl = currentUser.getUrl();
            String email = Plus.AccountApi.getAccountName(googleApiClient);
            String id = currentUser.getId();

            System.out.println(String.format("Id: %s\nFullname: %s\nPhoto URL: %s\nProfile URL: %s\nEmail: %s",id,fullname,photoUrl,profileUrl,email));

            Bundle googleBundle = new Bundle();
            googleBundle.putString(Constants.FULLNAME,fullname);
            googleBundle.putString(Constants.PHOTO_URL,photoUrl);
            googleBundle.putString(Constants.PROFILE_URL,profileUrl);
            googleBundle.putString(Constants.EMAIL,email);
            googleBundle.putString(Constants.ID,id);

            if( googleApiClient != null ) {
                googleApiClient.disconnect();
            }

            AppEventBus.post(new GoogleLoginEvent(googleBundle));
        }

        else {
            System.out.println("Google account not found");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Google+ connection suspended");
        if( googleApiClient != null ) {
            googleApiClient.disconnect();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Google+ connection failed");

        if( ! isResolving && shouldResolve ) {
            if (connectionResult.hasResolution()) {
                isResolving = true;
                AppEventBus.post(new GoogleResolutionEvent(connectionResult));
            } else {
                AppEventBus.post(new GoogleErrorEvent(connectionResult));
            }
        }
    }
}
