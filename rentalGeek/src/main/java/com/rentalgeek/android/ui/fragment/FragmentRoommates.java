package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;


public class FragmentRoommates  extends GeekBaseFragment {

    private static final String TAG = FragmentRoommates.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_roommates, container, false);
        ButterKnife.inject(this, v);
        return v;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        fetchRoommateGroups(ApiManager.currentUser.roommate_group_id);
    }

    protected void bindRoommateGroups() {

    }

    protected void fetchRoommateGroups(String groupId) {
        GlobalFunctions.getApiCall(getActivity(), ApiManager.getRoommateGroups(groupId), AppPreferences.getAuthToken(),
                new GeekHttpResponseHandler() {

                    @Override
                    public void onBeforeStart() {
                        showProgressDialog(R.string.dialog_msg_loading);
                    }

                    @Override
                    public void onFinish() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onSuccess(String content) {
                        try {
                            LoginBackend detail = (new Gson()).fromJson(content, LoginBackend.class);
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
