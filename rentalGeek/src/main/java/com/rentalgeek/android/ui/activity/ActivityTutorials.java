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
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.rentalgeek.android.R;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.ui.AppPrefes;
import com.rentalgeek.android.ui.adapter.SwipeAdapter;
import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityTutorials extends GeekBaseActivity {

	// Linked in
	private static final String TAG = ActivityTutorials.class.getSimpleName();
	public static final String PACKAGE_MOBILE_SDK_SAMPLE_APP = "com.rentalgeek.android";

	private static final String host = "api.linkedin.com";
	private static final String topCardUrl = "https://"
			+ host
			+ "/v1/people/~:(first-name,last-name,picture-url,id,email-address)";
	private static final String shareUrl = "https://" + host
			+ "/v1/people/~/shares";

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
		setUpdateState();
		getKeyHash(this, PACKAGE_MOBILE_SDK_SAMPLE_APP);
		//containerlogin = (FrameLayout) findViewById(R.id.containertut);
		mAdapter = new SwipeAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		appPref = new AppPrefes(this, "rentalgeek");
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		// mPager.beginFakeDrag();
		// mPager.fakeDragBy(5);

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

		LISessionManager.getInstance(getApplicationContext()).onActivityResult(
				this, requestCode, resultCode, data);
		if (requestCode == FragmentSignIn.RC_SIGN_IN) {
			FragmentSignIn fragment = (FragmentSignIn) getSupportFragmentManager()
					.findFragmentById(R.id.pager);
			fragment.onActivityResult(requestCode, resultCode, data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	// Linked in essentials
	private void setUpdateState() {
		LISessionManager sessionManager = LISessionManager
				.getInstance(getApplicationContext());
		LISession session = sessionManager.getSession();
		boolean accessTokenValid = session.isValid();

	}

	private static Scope buildScope() {
		return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
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

	public void SignInLinkedIn() {
		LISessionManager.getInstance(getApplicationContext()).init(
				this, buildScope(), new AuthListener() {
					@Override
					public void onAuthSuccess() {
						setUpdateState();

						APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
						apiHelper.getRequest(ActivityTutorials.this, topCardUrl,
								new ApiListener() {
									@Override
									public void onApiSuccess(ApiResponse s) {

										System.out.println("linked in response " + s.getResponseDataAsJson());

										FragmentSignIn fragment = (FragmentSignIn) getSupportFragmentManager().findFragmentById(R.id.pager);

										try {

											appPref.SaveData(
													"socialname_link",
													s.getResponseDataAsJson()
															.optString(
																	"firstName"));
											
											appPref.SaveData(
													"sociallastname_link",
													s.getResponseDataAsJson()
															.optString(
																	"lastName"));
											appPref.SaveData(
													"socialemail_link",
													s.getResponseDataAsJson()
															.optString(
																	"emailAddress"));
											appPref.SaveData("socialid_link", s
													.getResponseDataAsJson()
													.optString("id"));


											fragment.callLinkedPlusLink(
													s.getResponseDataAsJson()
															.getString(
																	"firstName"),
													"",
													s.getResponseDataAsJson()
															.getString("id"),
													s.getResponseDataAsJson()
															.getString(
																	"emailAddress"));
										} catch (JSONException e) {
											AppLogger.log(TAG, e);
										}

									}

									@Override
									public void onApiError(LIApiError error) {
                                        AppLogger.log(TAG, error);
									}
								});

					}

					@Override
					public void onAuthError(LIAuthError error) {
						setUpdateState();

						Toast.makeText(getApplicationContext(),
								"failed " + error.toString(), Toast.LENGTH_LONG)
								.show();
					}
				}, true);
	}

}
