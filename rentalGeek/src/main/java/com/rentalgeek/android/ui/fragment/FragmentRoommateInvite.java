package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.RoommateInvites;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentRoommateInvite extends GeekBaseFragment {

    private static final String TAG = FragmentRoommateInvite.class.getSimpleName();

    @InjectView(R.id.textViewInviteMessage)
    TextView textViewInviteMessage;

    @InjectView(R.id.layoutInviteButtons)
    LinearLayout layoutInviteButtons;

    protected int roommateInviteId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommate_invite, container, false);
        ButterKnife.inject(this, v);

        roommateInviteId = getArguments().getInt(Common.KEY_ROOMMATE_INVITE_ID);

        //if (SessionManager.Instance.getCurrentUser() != null)// && ApiManager.currentUser.roommate_group_id != null)
        fetchRoommateInvites(String.valueOf(roommateInviteId));

        return v;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    protected void bindRoommateInvites(RoommateInvites roommateInvites) {
        if (roommateInvites != null) {
            if (ListUtils.isNullOrEmpty(roommateInvites.roommate_invites)) {
                textViewInviteMessage.setText(R.string.fragment_roommateinvite_noinvites);
                layoutInviteButtons.setVisibility(View.GONE);
            } else {
                layoutInviteButtons.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void fetchRoommateInvites(String roommateInviteId) {
        String url =  ApiManager.getRoommateInvites(roommateInviteId);
        GlobalFunctions.getApiCall(getActivity(), url, AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

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
                        try {
                            RoommateInvites roommateInvites = (new Gson()).fromJson(content, RoommateInvites.class);
                            if (roommateInvites != null) {
                                bindRoommateInvites(roommateInvites);
                            }
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }

                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                        DialogManager.showCrouton(activity, failureResponse);
                    }
                });
    }


}
