package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.RoommateGroup;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FragmentRoommates  extends GeekBaseFragment {

    private static final String TAG = FragmentRoommates.class.getSimpleName();

    @InjectView(R.id.editTextFirstLastName)
    EditText editTextFirstLastName;

    @InjectView(R.id.editTextEmailAddress)
    EditText editTextEmailAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommates, container, false);
        ButterKnife.inject(this, v);
        return v;

    }

    @OnClick(R.id.buttonAddRoommate)
    public void clickbuttonAddRoommate() {
        String name = editTextFirstLastName.getText().toString();
        String email = editTextEmailAddress.getText().toString();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
            addRoommateInvite(email, name);
        }
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (ApiManager.currentUser != null && ApiManager.currentUser.roommate_group_id != null)
           fetchRoommateGroups(ApiManager.currentUser.roommate_group_id);
    }

    protected void bindRoommateGroups(RoommateGroup roommateGroup) {

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
                            String response = content;
//                            RoommateGroup roommateGroup = (new Gson()).fromJson(content, RoommateGroup.class);
//                            if (roommateGroup != null) {
//                                bindRoommateGroups(roommateGroup);
//                            }
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex, String failureResponse) {
                        super.onFailure(ex, failureResponse);
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
                            RoommateGroup roommateGroup = (new Gson()).fromJson(content, RoommateGroup.class);
                            if (roommateGroup != null) {
                                bindRoommateGroups(roommateGroup);
                            }
                        } catch (Exception e) {
                            AppLogger.log(TAG, e);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                    }
                });
    }
}
