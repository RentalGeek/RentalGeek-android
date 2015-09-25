package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.api.ApiManager;

/**
 * Created by rajohns on 9/12/15.
 *
 */
public class FragmentApprovedProperties extends FragmentBaseApplicationList {

    private boolean alreadyMadeApplicationsApiCall = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setupRecyclerView(getActivity(), false/*, ApplicationListAdapter.PROPERTIES*/);

        return v;
    }

    /**
     * Wait until user comes to this tab before making the
     * networking call. Also only make the networking call
     * the first time the user comes to the tab.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !alreadyMadeApplicationsApiCall) {
            alreadyMadeApplicationsApiCall = true;
            fetchItemsWithUrl(getActivity(), ApiManager.getApprovedApplicationsUrl());
        }
    }
}
