package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorObj;
import com.rentalgeek.android.backend.RoommateInviteResponse;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentRoommateInvite extends GeekBaseFragment {

    private static final String TAG = FragmentRoommateInvite.class.getSimpleName();

    @InjectView(R.id.textViewInviteMessage)
    TextView textViewInviteMessage;

    @InjectView(R.id.layoutInviteButtons)
    LinearLayout layoutInviteButtons;

    protected int roommateGropuId;
    protected int roommateInviteId;
    protected int roommateInviterId;
    protected String roommateInviterName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommate_invite, container, false);
        ButterKnife.inject(this, v);

        Bundle args = getArguments();

        roommateGropuId = args.getInt(Common.KEY_ROOMMATE_GROUP_ID);
        roommateInviteId = args.getInt(Common.KEY_ROOMMATE_INVITE_ID);
        roommateInviterId = args.getInt(Common.KEY_ROOMMATE_INVITER_ID);
        roommateInviterName = args.getString(Common.KEY_ROOMMATE_INVITER_NAME);

        //if (SessionManager.Instance.getCurrentUser() != null)// && ApiManager.currentUser.roommate_group_id != null)
        //fetchRoommateInvites(String.valueOf(roommateInviteId));

        return v;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindRoommateInvite();
    }

    protected void bindRoommateInvite() {

        if (TextUtils.isEmpty(roommateInviterName)) {
            textViewInviteMessage.setText(R.string.fragment_roommateinvite_noinvites);
            layoutInviteButtons.setVisibility(View.GONE);
        } else {
            textViewInviteMessage.setText(Html.fromHtml(RentalGeekApplication.getResourceString(R.string.fragment_roommateinvite_message, roommateInviterName)));
            layoutInviteButtons.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.buttonAccept)
    void submitButtonAccept() {
        inviteAcceptDecline(true, roommateInviteId);
    }

    @OnClick(R.id.buttonDecline)
    void submitButtonDecline() {
        inviteAcceptDecline(false, roommateInviteId);
    }


    protected void inviteAcceptDecline(boolean isAccept, int inviteId) {

        RequestParams params = new RequestParams();

        params.put("roommate_invite[roommate_group_id]", String.valueOf(inviteId));

        try {

            String url = isAccept ? ApiManager.getRoommateInviteAccept(String.valueOf(inviteId)) : ApiManager.getRoommateInviteDeny(String.valueOf(inviteId));

            GlobalFunctions.postApiCall(activity, url, params, AppPreferences.getAuthToken(),
                    new GeekHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            showProgressDialog(R.string.dialog_msg_loading);
                        }

                        @Override
                        public void onFinish() {
                            hideProgressDialog();
                        }

                        @Override
                        public void onSuccess(String content) {
                            try {
                                String response = content;
                                AppLogger.log(TAG, "response:" + content);

                                RoommateInviteResponse roommateInvite = (new Gson()).fromJson(content, RoommateInviteResponse.class);
                                if (roommateInvite != null && roommateInvite.roommate_invite != null) {
                                    SessionManager.Instance.getCurrentUser().setRoommateGroupId(String.valueOf(roommateInvite.roommate_invite.roommate_group_id));
                                    activity.finish();
                                } else {
                                    DialogManager.showCrouton(activity, "Error with invite.");
                                }
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String failureResponse) {
                            super.onFailure(ex, failureResponse);

                            try {
                                ErrorObj errorObj = (new Gson()).fromJson(failureResponse, ErrorObj.class);

//                                if (errorObj != null && errorObj.errors != null) {
//                                    if (!ListUtils.isNullOrEmpty(errorObj.errors.email)) {
//                                        OkAlert.show(getActivity(), "Email", errorObj.errors.email.get(0));
//                                    }
//
//                                }

                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }

                        }

                        @Override
                        public void onAuthenticationFailed() {

                        }
                    });

        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }
}
