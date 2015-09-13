package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.ApplicationDTO;
import com.rentalgeek.android.pojos.ApplicationDetailsDTO;
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.ui.adapter.ApplicationListAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/11/15.
 *
 */
public class FragmentCosignerProperties extends GeekBaseFragment {

    @InjectView(R.id.recyclerView) RecyclerView recyclerView;
    @InjectView(R.id.no_items_view) TextView noItemsView;
    List<ApplicationItem> cosignerProperties = new ArrayList<>();
    ApplicationListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cosigner_list, container, false);
        ButterKnife.inject(this, v);

        setupRecyclerView();
        fetchCosignerProperties();

        return v;
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ApplicationListAdapter(cosignerProperties, false);
        recyclerView.setAdapter(adapter);
    }

    private void fetchCosignerProperties() {
        GlobalFunctions.getApiCall(getActivity(), ApiManager.getCosignerItemsUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                parseResponse(content);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });
    }

    private void parseResponse(String response) {
        ApplicationDetailsDTO applicationDetailsDTO = new Gson().fromJson(response, ApplicationDetailsDTO.class);

        if (applicationDetailsDTO.applications.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noItemsView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noItemsView.setVisibility(View.GONE);
        }

        for (int i = 0; i < applicationDetailsDTO.applications.size(); i++) {
            ApplicationDTO applicationDTO = applicationDetailsDTO.applications.get(i);
            ApplicationItem applicationItem = new ApplicationItem(applicationDTO);
            cosignerProperties.add(applicationItem);
        }

        adapter.setItems(cosignerProperties);
        adapter.notifyDataSetChanged();
    }
}