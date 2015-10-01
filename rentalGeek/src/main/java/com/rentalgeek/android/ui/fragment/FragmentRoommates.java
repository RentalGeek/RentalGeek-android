package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.ErrorObj;
import com.rentalgeek.android.backend.RoommateGroupResponse;
import com.rentalgeek.android.backend.RoommateInviteResponse;
import com.rentalgeek.android.backend.model.RoommateGroup;
import com.rentalgeek.android.backend.model.RoommateInvite;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityRoommateInvite;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.utils.ListUtils;
import com.rentalgeek.android.utils.OkAlert;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FragmentRoommates  extends GeekBaseFragment {

    private static final String TAG = FragmentRoommates.class.getSimpleName();

    private String name;
    private String email;

    @InjectView(R.id.editTextFirstLastName)
    EditText editTextFirstLastName;

    @InjectView(R.id.editTextEmailAddress)
    EditText editTextEmailAddress;

    @InjectView(R.id.layoutRoommateInvites)
    LinearLayout layoutRoommateInvites;

    @InjectView(R.id.layoutRoommates)
    LinearLayout layoutRoommates;


    private String currentGroupId;
    private boolean isGroupOwner = false;
    private boolean isInviteNotified = false;

    private int currentRoommateCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommates, container, false);
        ButterKnife.inject(this, v);
        fetchGroups();
        return v;

    }

    @OnClick(R.id.buttonAddRoommate)
    public void clickbuttonAddRoommate() {
//        String name = editTextFirstLastName.getText().toString();
//        String email = editTextEmailAddress.getText().toString();

//        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
        if (validInput()) {
            if (currentRoommateCount <= 3) {
                addRoommateInvite(email, name);
            } else {
                DialogManager.showCrouton(activity, "Max of 4 Roommates allowed.");
            }
        }
//        }
    }

    @OnClick(R.id.buttonLeaveGroup)
    public void clickButtonLeaveGroup() {
        if (SessionManager.Instance.getCurrentUser() != null &&
                SessionManager.Instance.getCurrentUser().id != null &&
                !TextUtils.isEmpty(currentGroupId))
            removeRoommateGroupUser(currentGroupId, SessionManager.Instance.getCurrentUser().id);
    }

    protected void clearFormValues() {
        editTextFirstLastName.setText(null);
        editTextEmailAddress.setText(null);
    }

    protected void evaluateGroupOwner(RoommateGroup group) {
        try {
            isGroupOwner = String.valueOf(group.owner_id).equals(SessionManager.Instance.getCurrentUser().id);
        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    private boolean validInput() {
        name = editTextFirstLastName.getText().toString().trim();
        email = editTextEmailAddress.getText().toString().trim();

        if (name.equals("")) {
            OkAlert.show(getActivity(), "Name", "Please enter first and last name.");
            return false;
        }

        if (email.equals("")) {
            OkAlert.show(getActivity(), "Email", "Please enter email address.");
            return false;
        }

        return true;
    }

    protected void fetchGroups() {
        if (!TextUtils.isEmpty(SessionManager.Instance.getCurrentUser().roommate_group_id)) {
            fetchRoommateGroups(SessionManager.Instance.getCurrentUser().roommate_group_id);
            layoutRoommates.setVisibility(View.VISIBLE);
        } else {
           // DialogManager.showCrouton(activity, "You don't belong to a Roommate Group");
            //activity.finish();
            layoutRoommates.setVisibility(View.GONE);
        }

    }

    protected void bindRoommateInvite(RoommateInvite invite) {
        LinearLayout layoutListItem = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.layout_listitem_roommate_invite, null);
        TextView textViewInviteName = getView(R.id.textViewInviteName, layoutListItem);
        String label = String.format("%s %s", invite.invited_name, invite.accepted ? "" : "(Pending)");
        textViewInviteName.setText(label);
        layoutRoommateInvites.addView(layoutListItem);

        final int inviteId = invite.id;

        ImageView imageViewRemoveInvite = getView(R.id.imageViewRemoveInvite, layoutListItem);

        if (isGroupOwner) {
            imageViewRemoveInvite.setVisibility(View.VISIBLE);
            imageViewRemoveInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeRoommateInvite(String.valueOf(inviteId));
                }
            });
        } else {
            imageViewRemoveInvite.setVisibility(View.GONE);
        }

        layoutRoommates.setVisibility(View.VISIBLE);

    }

    protected void bindRoommateInvites(List<RoommateInvite> invites) {

        layoutRoommateInvites.removeAllViews();

        int roommateInviteId = -1;

        if (!ListUtils.isNullOrEmpty(invites)) {

            currentRoommateCount = invites.size();

            int currentUserId = Integer.valueOf(SessionManager.Instance.getCurrentUser().id);

            for (int i=0; i<invites.size(); i++) {

                RoommateInvite invite = invites.get(i);

                if (!invite.accepted && currentUserId == invite.invited_id && !isInviteNotified) {
                    isInviteNotified = true;
                    roommateInviteId = invites.get(i).id;
                    //break;
                }

                //if (currentUserId != invite.invited_id)
                    bindRoommateInvite(invites.get(i));
            }

            if (roommateInviteId >= 0) {

                Bundle args = new Bundle();
                args.putInt(Common.KEY_ROOMMATE_INVITE_ID, roommateInviteId);
                Navigation.navigateActivity(activity, ActivityRoommateInvite.class, args, false);
            }
        }
    }

    protected void removeRoommateGroupUser(String groupId, String userId) {

        try {

            GlobalFunctions.deleteApiCall(activity, ApiManager.getRoommateGroupRemoveUser(groupId, userId), AppPreferences.getAuthToken(),
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
                                Navigation.navigateActivity(activity, ActivityHome.class);
                                SessionManager.Instance.getCurrentUser().roommate_group_id = null;
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String failureResponse) {
                            super.onFailure(ex, failureResponse);
                            DialogManager.showCrouton(activity, failureResponse);
                        }

                        @Override
                        public void onAuthenticationFailed() {

                        }
                    });

        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    protected void removeRoommateInvite(String inviteId) {

        try {

            GlobalFunctions.deleteApiCall(activity, ApiManager.getRoommateInvites(inviteId), AppPreferences.getAuthToken(),
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
                                fetchGroups();
                            } catch (Exception e) {
                                AppLogger.log(TAG, e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable ex, String failureResponse) {
                            super.onFailure(ex, failureResponse);
                            DialogManager.showCrouton(activity, failureResponse);
                        }

                        @Override
                        public void onAuthenticationFailed() {

                        }
                    });

        } catch (Exception e) {
            AppLogger.log(TAG, e);
        }
    }

    protected void addRoommateInvite(String email, String name) {

        RequestParams params = new RequestParams();
        params.put("roommate_invite[email]", email);
        params.put("roommate_invite[name]", name);

        try {


        GlobalFunctions.postApiCall(activity, ApiManager.getRoommateInvites(""), params, AppPreferences.getAuthToken(),
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
                            RoommateInviteResponse roommateInvite = (new Gson()).fromJson(content, RoommateInviteResponse.class);
                            if (roommateInvite != null) {
                                bindRoommateInvite(roommateInvite.roommate_invite);
                                clearFormValues();
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

                            if (errorObj != null && errorObj.errors != null) {
                                if (!ListUtils.isNullOrEmpty(errorObj.errors.email)) {
                                    OkAlert.show(getActivity(), "Email", errorObj.errors.email.get(0));
                                }

                            }

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

    protected void fetchRoommateGroups(String groupId) {
        GlobalFunctions.getApiCall(activity, ApiManager.getRoommateGroups(groupId), AppPreferences.getAuthToken(),
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
                            RoommateGroupResponse roommateGroup = (new Gson()).fromJson(content, RoommateGroupResponse.class);
                            if (roommateGroup != null) {
                                currentGroupId = String.valueOf(roommateGroup.roommate_group.id);
                                evaluateGroupOwner(roommateGroup.roommate_group);
                                bindRoommateInvites(roommateGroup.roommate_group.roommate_invites);
                            }
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
                        Navigation.navigateActivity(activity, ActivityHome.class);
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }
}
