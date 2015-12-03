package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;


public class FragmentNotifications extends GeekBaseFragment {

    private static final String TAG = FragmentNotifications.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.inject(this, v);
        return v;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

//        if (ApiManager.currentUser != null && ApiManager.currentUser.roommate_group_id != null)
//           fetchRoommateGroups(ApiManager.currentUser.roommate_group_id);
    }

    protected void bindNotifications() {

    }

    protected void fetchNotifications(String groupId) {
//        GlobalFunctions.getApiCall(getActivity(), ApiManager.getRoommateGroups(groupId), AppPreferences.getAuthToken(),
//                new GeekHttpResponseHandler() {
//
//                    @Override
//                    public void onBeforeStart() {
//                        showProgressDialog(R.string.dialog_msg_loading);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        hideProgressDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(String content) {
//                        try {
//                            RoommateGroup roommateGroup = (new Gson()).fromJson(content, RoommateGroup.class);
//                            if (roommateGroup != null) {
//                                bindRoommateGroups(roommateGroup);
//                            }
//                        } catch (Exception e) {
//                            AppLogger.log(TAG, e);
//                        }
//                    }
//
//                    @Override
//                    public void onAuthenticationFailed() {
//
//                    }
//                });
    }
}
