package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.ApplicationDetails;
import com.rentalgeek.android.pojos.CosignItem;
import com.rentalgeek.android.ui.adapter.CosignerListAdapter;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ActivityCosignerList extends GeekBaseActivity {

    List<CosignItem> cosignItems = new ArrayList<>();
    CosignerListAdapter adapter = new CosignerListAdapter();

    public ActivityCosignerList() {
        super(true, true, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosigner_list);

        setupRecyclerView();
        fetchCosignItems();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CosignerListAdapter(cosignItems);
        recyclerView.setAdapter(adapter);
    }

    private void fetchCosignItems() {
//        cosignItems = Stub.cosignItems();
//        adapter.setItems(cosignItems);
//        adapter.notifyDataSetChanged();


        GlobalFunctions.getApiCall(this, ApiManager.getApplyUrl(), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
        ApplicationDetails applicationDetails = new Gson().fromJson(response, ApplicationDetails.class);
        // call CosignItem Constructor that accepts an ApplicationDetails
    }

}
