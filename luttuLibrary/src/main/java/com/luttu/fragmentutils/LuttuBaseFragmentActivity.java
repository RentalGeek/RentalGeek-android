package com.luttu.fragmentutils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.luttu.nettoast.Configuration;
import com.luttu.nettoast.Crouton;
import com.luttu.nettoast.Style;
import com.luttu.utils.NetworkChangeReceiver;
import com.luttu.utils.NetworkChangeReceiver.Internet;

public class LuttuBaseFragmentActivity extends FragmentActivity implements Internet {

	public AppPrefes appPrefes;
	public FrameLayout containerlogin;
	public LinearLayout toplay_click_cancel;
	OnBackButtonClickedListener backButtonClickedListener;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		appPrefes = new AppPrefes(this, "justask");
		NetworkChangeReceiver.internet = this;
	}

	public void showBuiltInCrouton() {
		String croutonText = "Please check your internet connection and try again";
		showCrouton(croutonText, Configuration.DEFAULT);
	}

	private void showCrouton(String croutonText, Configuration configuration) {
		final Crouton crouton;
		crouton = Crouton.makeText(this, croutonText, Style.ALERT);
		crouton.show();
	}

	@Override
	public void net() {
		System.out.println("inside net interface");
		showBuiltInCrouton();
	}

	@Override
	public void onBackPressed() {
		if (backButtonClickedListener != null)
			backButtonClickedListener.onBackButtonClicked();
		else
			super.onBackPressed();
	}

	public interface OnBackButtonClickedListener {
		void onBackButtonClicked();
	}

	public void backpress() {
		super.onBackPressed();
	}

}
