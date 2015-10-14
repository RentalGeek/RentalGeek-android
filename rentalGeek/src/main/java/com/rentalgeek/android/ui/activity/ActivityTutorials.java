package com.rentalgeek.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.linkedin.platform.LISessionManager;
import com.rentalgeek.android.R;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.adapter.SwipeAdapter;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityTutorials extends GeekBaseActivity {

	// Linked in
	private static final String TAG = ActivityTutorials.class.getSimpleName();
	public static final String PACKAGE_MOBILE_SDK_SAMPLE_APP = "com.rentalgeek.android";

	AppPrefes appPref;
	SwipeAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	Handler handler;
	Runnable Update;

	int currentPage = 0;
	int NUM_PAGES = 4;
	private Timer swipeTimer;

	public ActivityTutorials() {
		super(false, false, false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorials);
		getKeyHash(this, PACKAGE_MOBILE_SDK_SAMPLE_APP);
		mAdapter = new SwipeAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		appPref = new AppPrefes(this, "rentalgeek");
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		handler = new Handler();
		final Runnable Update = new Runnable() {
			public void run() {
				if (currentPage == NUM_PAGES) {
					remove();
				}
				mPager.setCurrentItem(currentPage++, true);
			}
		};

		swipeTimer = new Timer();
		swipeTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.post(Update);
			}
		}, 200, 4000);

		mPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				swipeTimer.cancel();
				return false;

			}
		});

	}

	public void remove() {
		handler.removeCallbacks(Update);
	}
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        System.out.println(String.format("Request code %d Result code %d",requestCode,resultCode));
        
        String currentFragmentTag = mAdapter.getCurrentFragmentTag();

        System.out.println("Current fragment tag is " + currentFragmentTag);

        if( currentFragmentTag != null ) {
            
            Fragment fragment = (Fragment)getSupportFragmentManager().findFragmentByTag(currentFragmentTag);

            if ( requestCode == FragmentSignIn.RC_SIGN_IN || requestCode == FragmentSignIn.GP_SIGN_IN ) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } else if (data != null && data.getAction() != null && data.getAction().equals("com.linkedin.thirdparty.authorize.RESULT_ACTION")) {
                fragment.onActivityResult(requestCode, resultCode, data);
            } 
        }

        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    
	public static String getKeyHash(Context context, String packageName) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				System.out.println("key linked hash is " + keyHash);
				return keyHash;
			}
		} catch (PackageManager.NameNotFoundException e) {
			AppLogger.log(TAG, e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			AppLogger.log(TAG, e);
			return null;
		}
		return null;
	}
}
