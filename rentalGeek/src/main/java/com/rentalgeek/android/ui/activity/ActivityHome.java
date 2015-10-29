package com.rentalgeek.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.ShowProfileCreationEvent;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.mvp.home.HomePresenter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;
import com.rentalgeek.android.utils.Analytics;
import com.rentalgeek.android.utils.CosignerInviteCaller;
import com.rentalgeek.android.utils.ObscuredSharedPreferences;

import java.util.ArrayList;


public class ActivityHome extends GeekBaseActivity implements Container<ViewPager> {

    private static String TAG = ActivityHome.class.getSimpleName();
    private HomePresenter presenter;
    private MapView mapView;
    private RentalListView rentalListView;
    private boolean shouldReload = false;

    public ActivityHome() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        shouldReload = true;
        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub, R.layout.pager_container);
        setTabs(true);
        setupNavigation();

        mapView = new FragmentMap();
        rentalListView = new FragmentRentalListView();

        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.container);

        if (viewPager != null) {
            setupContainer(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        presenter = new HomePresenter();

        // silently fetch cosigner invites to know which page to go to
        if (SessionManager.Instance.getCurrentUser() != null) {
            new CosignerInviteCaller(this, false).fetchCosignerInvites();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean shouldDoSilentUpdate = extras.getBoolean(Common.KEY_DO_SILENT_UPDATE, false);
            if (shouldDoSilentUpdate) {
                // silently fetch current user data in case persisted user data has gotten stale
                silentUserDataUpdate();
            }
        }

        disableDrawerGesture();

        Analytics.logUserLogin(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldReload = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgressDialog();

        if (shouldReload) {
            showProgressDialog(R.string.loading_rentals);
            final Bundle extras = getIntent().getExtras();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (extras == null) {
                        presenter.getRentalOfferings();
                    } else {
                        ArrayList<String> rental_ids = extras.getStringArrayList("RENTALS");
                        if (rental_ids == null) {
                            presenter.getRentalOfferings();
                        } else {
                            presenter.getRentalOfferings(extras);
                        }
                    }
                }
            }, 3000);
        }
    }

    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment((FragmentMap) mapView, "Map View");
        adapter.addFragment((FragmentRentalListView) rentalListView, "List View");
        container.setAdapter(adapter);
    }


    public void onEventMainThread(ClickRentalEvent event) {
        Bundle bundle = event.getBundle();

        if (bundle != null) {
            Intent intent = new Intent(this, ActivityRental.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void onEventMainThread(ShowProfileCreationEvent event) {
        Navigation.navigateActivity(this, ActivityCreateProfile.class);
    }

    private void silentUserDataUpdate() {
        ObscuredSharedPreferences prefs = new ObscuredSharedPreferences(this, this.getSharedPreferences("com.android.rentalgeek", Context.MODE_PRIVATE));
        String email = prefs.getString(ObscuredSharedPreferences.USERNAME_PREF, "");
        String password = prefs.getString(ObscuredSharedPreferences.PASSWORD_PREF, "");

        RequestParams params = new RequestParams();
        params.put("user[email]", email);
        params.put("user[password]", password);

        GlobalFunctions.postApiCall(this, ApiManager.getSignin(), params, AppPreferences.getAuthToken(),
            new GeekHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    try {
                        LoginBackend detail = new Gson().fromJson(content, LoginBackend.class);
                        SessionManager.Instance.onUserLoggedIn(detail);
                    } catch (Exception e) {
                        AppLogger.log(TAG, e);
                    }
                }
            });
    }

}
