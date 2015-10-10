package com.rentalgeek.android.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.database.PropertyTable;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.Navigation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivityMain extends GeekBaseActivity {

	AppPrefes appPref;

	public ActivityMain() {
		super(true, true, false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_page);
		appPref = new AppPrefes(getApplicationContext(), "rentalgeek");

		getHash();
		timer();

	}

	private void timer() {

        final AppCompatActivity activity = this;

		new CountDownTimer(4000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {


			}

			@Override
			public void onFinish() {
				if (!appPref.getData("first").equals("logged")) {
					appPref.SaveData("bysearch", "no");
					Navigation.navigateActivity(activity, ActivityTutorials.class, true);
				} else {

					if (appPref.getData("bysearch").equals("yes")) {
						new Delete().from(PropertyTable.class).execute();
					}

					appPref.SaveData("bysearch", "no");

					if (SessionManager.Instance.isUserLoggedIn()) {
						Navigation.navigateActivity(activity, ActivityHome.class, true);
					} else {
						Navigation.navigateActivity(activity, ActivityLogin.class, true);
					}


				}

			}
		}.start();
	}

	private void getHash() {

		PackageInfo info;
		try {
			// info = getPackageManager().getPackageInfo("com.blacktie",
			// PackageManager.GET_SIGNATURES);
			info = getPackageManager().getPackageInfo("com.rentalgeek.android", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				// String something = new
				// String(Base64.encodeBytes(md.digest()));
				Log.e("facebook hash key", something);
				System.out.println("hash key" + something);
			}
		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
	}

}
