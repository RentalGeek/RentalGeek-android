package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.view.InAppWebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentSignLease  extends GeekBaseFragment {

    public static final String STREET = "streetAddress";
    public static final String CITY_STATE_ZIP = "cityStateZipAddress";
    public static final String PDF_URL = "unsignedPdfUrl";

    @InjectView(R.id.street_address) TextView streetAddressTextView;
    @InjectView(R.id.city_state_zip_address) TextView cityStateZipAddressTextView;
    @InjectView(R.id.pdf_web_view) WebView pdfWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_lease, container, false);
        ButterKnife.inject(this, view);

        String street = getArguments().getString(STREET);
        String cityStateZip = getArguments().getString(CITY_STATE_ZIP);
        String pdfUrl = getArguments().getString(PDF_URL);

        streetAddressTextView.setText(street);
        cityStateZipAddressTextView.setText(cityStateZip);

        loadPdf(pdfUrl);

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
