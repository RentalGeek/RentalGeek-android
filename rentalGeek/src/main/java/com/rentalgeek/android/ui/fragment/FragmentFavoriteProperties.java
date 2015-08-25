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

import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseAbstract;
import com.rentalgeek.android.R;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.activity.ActivityHome;
import com.rentalgeek.android.utils.ConnectionDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentFavoriteProperties extends LuttuBaseAbstract {

	private static final String TAG = FragmentFavoriteProperties.class.getSimpleName();

	@InjectView(R.id.click_rent)
	TextView click_rent;

//	@InjectView(R.id.get_started_paid_already)
//	Button get_started_paid_already;
//
//	@InjectView(R.id.ddt)
//	TextView ddt;


//	@InjectView(R.id.get_started)
//	Button getStarted;

	AppPrefes appPref;
	ConnectionDetector con;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v=inflater.inflate(R.layout.fragment_message_geek_score, container,false);

		appPref=new AppPrefes(getActivity(), "rentalgeek");
		ButterKnife.inject(this,v);

		return v;
	}

	@Override
	public void parseresult(String response, boolean success, int value) {


	}

	@Override
	public void error(String response, int value) {


	}

	@OnClick(R.id.click_rent)
	public void ClickFinal()
	{
		try {
			appPref.SaveData("map_list", "");
			Navigation.navigateActivity(getActivity(), ActivityHome.class, true);
		} catch (Exception e) {
			AppLogger.log(TAG, e);
		}
	}


	@OnClick(R.id.infoclick2)
	public void infoclick2()
	{
		final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogInner);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.apply_confirm_dialog);

		TextView ok=(TextView) dialog.findViewById(R.id.ok_apply_dialog);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	@OnClick(R.id.infoclick1)
	public void infoclick1()
	{
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

				dialog.dismiss();
			}
		});

		dialog.show();

	}
}
