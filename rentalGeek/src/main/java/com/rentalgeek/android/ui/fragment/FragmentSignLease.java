package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.pojos.SignatureUrlDTO;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityCosignerList;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.ui.activity.ActivityProperties;
import com.rentalgeek.android.ui.preference.AppPreferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentSignLease  extends GeekBaseFragment {

    public static final String LEASE_ID = "leaseId";
    public static final String REQUESTING_FRAGMENT = "requestingFragment";

    private String requestingFragment;

    @InjectView(R.id.web_view) WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_lease, container, false);
        ButterKnife.inject(this, view);

        int leaseId = getArguments().getInt(LEASE_ID);
        requestingFragment = getArguments().getString(REQUESTING_FRAGMENT);

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
                SignatureUrlDTO signatureUrlDTO = new Gson().fromJson(content, SignatureUrlDTO.class);
                loadSignatureUrl(signatureUrlDTO.url);
            }

            @Override
            public void onFailure(Throwable ex, String failureResponse) {
                super.onFailure(ex, failureResponse);
            }
        });

        return view;
    }

    private void loadSignatureUrl(String signatureUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                if (url.equals("/success")) {
                    // take back to previous screen
                    // would also need to reload data so they can't click sign lease again
                    navigateToNextScreen();
                }

                if (url.contains("success")) {
                    Log.d("tag", "" + url);
                }
            }
        });

        webView.loadUrl(signatureUrl);
    }

    private void navigateToNextScreen() {
        if (requestingFragment.equals(FragmentBaseApplicationList.PENDING_PROPERTIES) || requestingFragment.equals(FragmentBaseApplicationList.APPROVED_PROPERTIES)) {
            Navigation.navigateActivity(getActivity(), ActivityProperties.class);
        } else if (requestingFragment.equals(FragmentBaseApplicationList.COSIGNER_PROPERTIES)) {
            Navigation.navigateActivity(getActivity(), ActivityCosignerList.class);
        } else {
            Navigation.navigateActivity(getActivity(), ActivityHome.class);
        }
    }

}
