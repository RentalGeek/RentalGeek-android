package com.rentalgeek.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rentalgeek.android.R;
import com.luttu.fragmentutils.AppPrefes;
import com.luttu.fragmentutils.LuttuBaseFragmentActivity;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.ui.fragment.FragmentListViewDetails;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * @author George
 * 
 * @purpose Base Activity which handles all the child fragments. This is the
 *          main Activity from which they we show the FragmentMap Fragment
 *
 */

public class ActivityHome extends LuttuBaseFragmentActivity {
   
	boolean doubleBackToExitPressedOnce;

	private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {

			// Bundle bundle = new Bundle();
			// bundle.putString("msg", "restart");
			Intent intent = new Intent(getApplicationContext(),
					ActivityHome.class);
			// intent.putExtras(bundle);
			startActivity(intent);
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	};

	private DrawerLayout mDrawerLayout;
	AppPrefes appPref;

	@InjectView(R.id.map_selector)
	View map_selector;

	@InjectView(R.id.list_selector)
	View list_selector;

	@Override
	protected void onCreate(Bundle arg0) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(arg0);

		appPref = new AppPrefes(getApplicationContext(), "rentalgeek");
		setListMapSelector();
		if (arg0 == null) {
			setContentView(R.layout.home_activity);
			ButterKnife.inject(this);
			 Thread.getDefaultUncaughtExceptionHandler();
			 Thread.setDefaultUncaughtExceptionHandler(handler);
			
//			 Thread.setDefaultUncaughtExceptionHandler(new
//					 UncaughtExceptionReporter(this));
			 
			 
			containerlogin = (FrameLayout) findViewById(R.id.containerlogin);
			toplay_click_cancel = (LinearLayout) findViewById(R.id.toplay_click_cancel);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			addfg(new FragmentMap());

			if (mDrawerLayout != null && mDrawerLayout instanceof DrawerLayout) {
				mDrawerLayout = (DrawerLayout) mDrawerLayout;
				mDrawerLayout.setDrawerListener(new DrawerListener() {
					@Override
					public void onDrawerSlide(View view, float v) {

					}

					@Override
					public void onDrawerOpened(View view) {

						hidekey();

					}

					@Override
					public void onDrawerClosed(View view) {
						hidekey();

					}

					@Override
					public void onDrawerStateChanged(int i) {

					}
				});
			}

		} else {
			System.out.println("oncreate attach");
			setContentView(R.layout.home_activity);
			ButterKnife.inject(this);
			toplay_click_cancel = (LinearLayout) findViewById(R.id.toplay_click_cancel);
			containerlogin = (FrameLayout) findViewById(R.id.containerlogin);
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		}

	}

	public void addfg(Fragment fragment) {

		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, fragment).commitAllowingStateLoss();
	}

	public void Left() {

		if (mDrawerLayout == null) {
			mDrawerLayout.openDrawer(Gravity.START);
		} else {
			mDrawerLayout.openDrawer(Gravity.START);
		}
	}

	public void Right() {

		if (mDrawerLayout == null) {
			mDrawerLayout.openDrawer(Gravity.END);
		} else {
			mDrawerLayout.openDrawer(Gravity.END);
		}
	}

	public void closedrawer() {

		// if (mDrawerLayout == null)
		// mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.closeDrawers();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("inside this finish 1" + requestCode);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");

				if (result.equals("true")) {
					System.out.println("inside this finish 2");
					finish();
					startActivity(getIntent());
				}

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	@OnClick(R.id.imageView_menu)
	public void ImageView_menu() {
		// if (mDrawerLayout == null){
		// closedrawer();
		// }else{

		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			closedrawer();
		} else {
			closedrawer();
			Left();
		}

		// }

	}

	@OnClick(R.id.imageView_lens_text)
	public void ImageView_lens() {
		// if (mDrawerLayout == null){
		// closedrawer();
		// }else{
		if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
			closedrawer();
		} else {
			closedrawer();
			Right();
		}
		// }
	}

	@OnClick({ R.id.map_lay, R.id.list_lay })
	public void ListMapSelection(View v) {
		switch (v.getId()) {
		case R.id.map_lay:
			closedrawer();
			if (!appPref.getData("map_list").equals("map")) {
				appPref.SaveData("map_list", "map");
				addfg(new FragmentMap());
				map_selector.setVisibility(View.VISIBLE);
				list_selector.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.list_lay:
			closedrawer();
			if (!appPref.getData("map_list").equals("list")) {
				appPref.SaveData("map_list", "list");
				addfg(new FragmentListViewDetails());
				map_selector.setVisibility(View.INVISIBLE);
				list_selector.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	private void setListMapSelector() {


		appPref.SaveData("map_list", "map");

	}

	protected void hidekey() {

		InputMethodManager imm = (InputMethodManager) ActivityHome.this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(ActivityHome.this.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public void selectorShift() {
		map_selector.setVisibility(View.INVISIBLE);
		list_selector.setVisibility(View.VISIBLE);
	}
	
	public void selectorShiftIn() {
		map_selector.setVisibility(View.VISIBLE);
		list_selector.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {

		 if (mDrawerLayout == null)
				mDrawerLayout.closeDrawers();
		FragmentManager fm = ActivityHome.this.getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			super.onBackPressed();
			return;
		} else {
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
				return;
			}

			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit",
					Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 2000);
		}

	}

}
