package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.activity.ActivityLogin;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.OkAlert;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rajohns on 9/21/15.
 *
 */
public class FragmentSettings extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.resubmit_button)
    public void resubmitButtonTapped() {
        String profileId = SessionManager.Instance.getCurrentUser().profile_id;
        GlobalFunctions.deleteApiCall(getActivity(), ApiManager.deleteProfile(profileId), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog(R.string.dialog_msg_loading);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                SessionManager.Instance.getCurrentUser().profile_id = null;
                Navigation.navigateActivity(activity, ActivityCreateProfile.class);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                DialogManager.showCrouton(activity, failureResponse);
            }
        });
    }

    @OnClick(R.id.delete_button)
    public void deleteAccountButtonTapped() {
        String userId = SessionManager.Instance.getCurrentUser().id;
        GlobalFunctions.deleteApiCall(getActivity(), ApiManager.specificUserUrl(userId), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog(R.string.dialog_msg_loading);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                SessionManager.Instance.onUserLoggedOut();
                Navigation.navigateActivity(getActivity(), ActivityLogin.class);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                OkAlert.show(getActivity(), "Error", "There was an error deleting your account. Please try again later.");
            }
        });
    }

}
