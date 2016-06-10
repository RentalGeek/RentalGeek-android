package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.backend.LeaseResponse;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.rentalgeek.android.constants.IntentKey.*;

public class FragmentViewLease extends GeekBaseFragment {

    @InjectView(R.id.web_view) WebView pdfWebView;
    @InjectView(R.id.error_text_view) TextView errorTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lease, container, false);
        ButterKnife.inject(this, view);

        int leaseId = getArguments().getInt(LEASE_ID);

        GlobalFunctions.getApiCall(ApiManager.getLease(Integer.toString(leaseId)), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                LeaseResponse leaseResponse = new Gson().fromJson(content, LeaseResponse.class);
                loadPdf(leaseResponse.lease.lease_document_url);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
                pdfWebView.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void loadPdf(String pdfUrl) {
        pdfWebView.getSettings().setJavaScriptEnabled(true);
        pdfWebView.getSettings().setBuiltInZoomControls(true);
        pdfWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        String googleDocs = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
        pdfWebView.loadUrl(googleDocs + pdfUrl);
    }
}
