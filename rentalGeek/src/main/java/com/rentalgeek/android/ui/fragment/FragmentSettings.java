package com.rentalgeek.android.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.User;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCreateProfile;
import com.rentalgeek.android.ui.activity.ActivityLogin;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ActionAlert;
import com.rentalgeek.android.utils.OkAlert;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.rentalgeek.android.constants.IntentKey.*;

public class FragmentSettings extends GeekBaseFragment {

    @InjectView(R.id.resubmit_geek_score_linlayout) LinearLayout resubmitGeekScoreLinLayout;
    @InjectView(R.id.description_textview) TextView descriptionTextView;
    @InjectView(R.id.note_charge_textview) TextView noteChargeTextView;
    @InjectView(R.id.resubmit_button) Button resubmitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, view);

        User currentUser = SessionManager.Instance.getCurrentUser();
        if (currentUser.is_cosigner) {
            resubmitGeekScoreLinLayout.setVisibility(View.GONE);
        } else if (!currentUser.hasProfileId()) {
            descriptionTextView.setText("Click below to submit your Geek Score application.");
            noteChargeTextView.setVisibility(View.GONE);
            resubmitButton.setText("SUBMIT");
        }

        return view;
    }

    @OnClick(R.id.resubmit_button)
    public void resubmitButtonTapped() {
        Intent intent = new Intent(activity, ActivityCreateProfile.class);
        intent.putExtra(RESUBMITTING_PROFILE, true);
        startActivity(intent);
    }

    @OnClick(R.id.delete_button)
    public void deleteAccountButtonTapped() {
        ActionAlert.show(getActivity(), "Delete Account", "Are you sure you want to delete your account?", "Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeDeleteAccountApiCall();
            }
        });
    }

    private void makeDeleteAccountApiCall() {
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
                AppPreferences.removeProfile();
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
