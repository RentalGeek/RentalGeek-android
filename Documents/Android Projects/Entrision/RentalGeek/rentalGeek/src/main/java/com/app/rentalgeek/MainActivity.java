package com.app.rentalgeek;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.app.rentalgeek.database.PropertyTable;
import com.app.rentalgeek.homepage.HomeActivity;
import com.app.rentalgeek.tutorials.Tutorials;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseFragmentActivity;

public class MainActivity extends LuttuBaseFragmentActivity {

	AppPrefes appPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_page);
		appPref = new AppPrefes(getApplicationContext(), "rentalgeek");
		getHash();
		timer();

	}

	private void timer() {
		// TODO Auto-generated method stub
		new CountDownTimer(4000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				if (!appPref.getData("first").equals("logged")) {
					appPref.SaveData("bysearch", "no");
					finish();
					Intent j = new Intent(MainActivity.this, Tutorials.class);
					startActivity(j);
					overridePendingTransition(R.anim.one_, R.anim.two_);
				} else {

					if (appPref.getData("bysearch").equals("yes")) {
						new Delete().from(PropertyTable.class).execute();
					}

					appPref.SaveData("bysearch", "no");
					MainActivity.this.finish();
					Intent i = new Intent(MainActivity.this, HomeActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.one_, R.anim.two_);

				}

			}
		}.start();
	}

	private void getHash() {
		// TODO Auto-generated method stub
		PackageInfo info;
		try {
			// info = getPackageManager().getPackageInfo("com.blacktie",
			// PackageManager.GET_SIGNATURES);
			info = getPackageManager().getPackageInfo("com.app.rentalgeek",
					PackageManager.GET_SIGNATURES);
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
