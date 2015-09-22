package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.InAppWebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentSignLease  extends GeekBaseFragment {

    public static final String LEASE_ID = "leaseId";

    @InjectView(R.id.pdf_web_view) WebView pdfWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_lease, container, false);
        ButterKnife.inject(this, view);

        int leaseId = getArguments().getInt(LEASE_ID);

        GlobalFunctions.getApiCall(getActivity(), ApiManager.signLeaseUrl(leaseId), AppPreferences.getAuthToken(), new GeekHttpResponseHandler() {
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
                Log.d("tag", "" + content);
                // load webview with url returned
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });

        return view;
    }

    private void loadPdf(String pdfUrl) {
        pdfWebView.getSettings().setJavaScriptEnabled(true);
        pdfWebView.getSettings().setBuiltInZoomControls(true);
        pdfWebView.setWebViewClient(new InAppWebViewClient());

        String googleDocs = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
        pdfWebView.loadUrl(googleDocs + pdfUrl);
    }

}
