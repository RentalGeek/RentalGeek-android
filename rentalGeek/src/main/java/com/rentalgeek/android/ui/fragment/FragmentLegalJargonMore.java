package com.rentalgeek.android.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.rentalgeek.android.R;
import com.rentalgeek.android.utils.ConnectionDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentLegalJargonMore extends LuttuBaseAbstract {

	/**
	 * @author george
	 * 
	 * @purpose This page shows the user more about the terms and condition the
	 *          user has to comply to use this application
	 */
	@InjectView(R.id.more_terms)
	WebView wv;
	ConnectionDetector con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View v = inflater.inflate(R.layout.fragment_legal_jargon_more, container, false);
		ButterKnife.inject(this, v);
		wv.loadUrl("file:///android_asset/termstwo.html");
		con = new ConnectionDetector(getActivity());
		return v;
	}

	@Override
	public void parseresult(String response, boolean success, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void error(String response, int value) {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.agree)
	public void agree() {

		if (con.isConnectingToInternet()) {
			nextfragment(new FragmentPayment(), false, R.id.container);
		}
		else
		{
			toast("Please check you internet connection");
		}

	}

	@OnClick(R.id.infoclick2)
	public void infoclick2() {
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogInner);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.apply_confirm_dialog);

		TextView ok = (TextView) dialog.findViewById(R.id.ok_apply_dialog);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();

			}
		});

		dialog.show();

	}

	@OnClick(R.id.infoclick1)
	public void infoclick1() {
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogInner);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.terms_dialog);

		WebView wv;
		TextView close;
		wv = (WebView) dialog.findViewById(R.id.terms_web);
		close = (TextView) dialog.findViewById(R.id.close);
		wv.loadUrl("file:///android_asset/terms.html");

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}

}
