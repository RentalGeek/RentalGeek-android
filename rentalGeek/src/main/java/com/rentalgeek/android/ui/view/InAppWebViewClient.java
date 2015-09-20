package com.rentalgeek.android.ui.view;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class InAppWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

}
