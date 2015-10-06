package com.rentalgeek.android.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.ApplicationDTO;
import com.rentalgeek.android.pojos.ApplicationDetailsDTO;
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.pojos.CosignItem;
import com.rentalgeek.android.ui.adapter.ApplicationListAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/13/15.
 *
 */
public class FragmentBaseApplicationList extends GeekBaseFragment {

    public static final String PENDING_PROPERTIES = "pendingProperties";
    public static final String APPROVED_PROPERTIES = "approvedProperties";
    public static final String COSIGNER_PROPERTIES = "cosignerProperties";

    @InjectView(R.id.recyclerView) RecyclerView recyclerView;
    @InjectView(R.id.no_items_view) TextView noItemsView;
    List<ApplicationItem> properties = new ArrayList<>();
    ApplicationListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    protected void setupRecyclerView(Context context, String requestingFragment, FragmentCosignerProperties fragment) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ApplicationListAdapter(properties, requestingFragment, fragment);
        recyclerView.setAdapter(adapter);
    }

    protected void setupRecyclerView(Context context, String requestingFragment) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ApplicationListAdapter(properties, requestingFragment);
        recyclerView.setAdapter(adapter);
    }

    protected void fetchItemsWithUrl(Context context, String url) {
        properties.clear();
        GlobalFunctions.getApiCall(context, url, AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                showNoResults();
            }
        });
    }

    private void parseResponse(String response) {
        ApplicationDetailsDTO applicationDetailsDTO = new Gson().fromJson(response, ApplicationDetailsDTO.class);

        if (applicationDetailsDTO.applications.size() == 0) {
            showNoResults();
        } else {
            showRecyclerView();
        }

        for (int i = 0; i < applicationDetailsDTO.applications.size(); i++) {
            ApplicationDTO applicationDTO = applicationDetailsDTO.applications.get(i);
            ApplicationItem applicationItem = itemFromType(applicationDTO);
            properties.add(applicationItem);
        }

        adapter.setItems(properties);
        adapter.notifyDataSetChanged();
    }

    private ApplicationItem itemFromType(ApplicationDTO applicationDTO) {
        if (this instanceof FragmentCosignerProperties) {
            return new CosignItem(applicationDTO);
        } else {
            return new ApplicationItem(applicationDTO);
        }
    }

    private void showNoResults() {
        recyclerView.setVisibility(View.GONE);
        noItemsView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        noItemsView.setVisibility(View.GONE);
    }

}
