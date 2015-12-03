package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;

/**
 * Created by rajohns on 9/11/15.
 */
public class FragmentCosignerProperties extends FragmentBaseApplicationList {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setupRecyclerView(getActivity(), FragmentBaseApplicationList.COSIGNER_PROPERTIES, this);
        fetchItemsWithUrl(getActivity(), ApiManager.getCosignerItemsUrl());

        return v;
    }

    public void applyToProperty(Integer rentalOfferingId) {
        RequestParams params = new RequestParams();
        params.put("application[rental_offering_id]", rentalOfferingId.toString());
        params.put("application[as_cosigner]", "true");

        GlobalFunctions.postApiCall(getActivity(), ApiManager.postApplication(), params, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                fetchItemsWithUrl(getActivity(), ApiManager.getCosignerItemsUrl());
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });
    }

}
