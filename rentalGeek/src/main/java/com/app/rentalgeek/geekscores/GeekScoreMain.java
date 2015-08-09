package com.app.rentalgeek.geekscores;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.app.rentalgeek.R;
import com.app.rentalgeek.backend.LoginBackend;
import com.app.rentalgeek.homepage.HomeActivity;
import com.app.rentalgeek.homepage.ListViewDetails;
import com.app.rentalgeek.utils.ConnectionDetector;
import com.app.rentalgeek.utils.StaticClass;
import com.google.gson.Gson;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;

public class GeekScoreMain extends LuttuBaseAbstract {

	/**
	 * @author george
	 * 
	 * @purpose This page introduces the user to the geek score application
	 *          process
	 */
	@InjectView(R.id.click_rent)
	TextView click_rent;

	@InjectView(R.id.get_started_paid_already)
	Button get_started_paid_already;

	@InjectView(R.id.ddt)
	TextView ddt;

	ConnectionDetector con;

	@InjectView(R.id.get_started)
	Button getStarted;

	AppPrefes appPref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.geekscore_main, container, false);
		ButterKnife.inject(this, v);
		con = new ConnectionDetector(getActivity());
		appPref = new AppPrefes(getActivity(), "rentalgeek");
		click_rent.setText(Html
				.fromHtml("<b>Click to Rent</b></font><sup>&#8482;</sup>"));

		if (con.isConnectingToInternet()) {
			CheckPaymentf();
		} else {
			toast("Please check you internet connection");
		}

		return v;
	}

	private void CheckPaymentf() {
		// TODO Auto-generated method stub

		// asynkhttpGet(2, StaticClass.headlink + "/v2/transactions", true);
		asynkhttpGet(
				3,
				StaticClass.headlink + "/v2/applicants/"
						+ appPref.getData("Uid"), true);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		super.onDestroyView();

		ButterKnife.reset(this);
	}

	@Override
	public void parseresult(String response, boolean success, int value) {
		// TODO Auto-generated method stub

		switch (value) {
		case 3:
			PaymentCheckParseNew(response);
			break;

		default:
			break;
		}

	}

	private void PaymentCheckParseNew(String response) {
		// TODO Auto-generated method stub

		try {
			LoginBackend detail = (new Gson()).fromJson(response,
					LoginBackend.class);

			if (detail.applicant.payment) {
				toastsuccess("Payment status: Complete");
				getStarted.setEnabled(false);
				ddt.setVisibility(View.INVISIBLE);
				get_started_paid_already.setVisibility(View.VISIBLE);
				click_rent.setVisibility(View.GONE);
				getStarted.setVisibility(View.GONE);
				appPref.SaveData("hasPay", "yes");

			} else {

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void error(String response, int value) {
		// TODO Auto-generated method stub

	}

	@OnClick(R.id.get_started)
	public void getStarted() {
		showDialog();
	}

	private void showDialog() {
		// TODO Auto-generated method stub

		try {
			final Dialog dialog = new Dialog(getActivity(),
					R.style.MyDialogInner);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.apply_confirm_dialog);

			TextView ok = (TextView) dialog.findViewById(R.id.ok_apply_dialog);

			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					dialog.dismiss();
					nextfragment(new LegalJargon(), false, R.id.container);

				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	@OnClick(R.id.get_started_paid_already)
	public void ClickRent() {

		((HomeActivity) getActivity()).selectorShift();
		appPref.SaveData("map_list", "");
		nextfragment(new ListViewDetails(), false, R.id.container);
	}

}
