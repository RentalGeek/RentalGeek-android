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
public class FragmentPendingProperties extends FragmentBaseApplicationList {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setupRecyclerView(getActivity(), FragmentBaseApplicationList.PENDING_PROPERTIES);
        fetchItemsWithUrl(getActivity(), ApiManager.getPendingApplicationsUrl());

        return v;
    }
}
