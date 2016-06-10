package com.rentalgeek.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.rentalgeek.android.R;
import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.LessThanMaxResultsReturnedEvent;
import com.rentalgeek.android.bus.events.MapChangedEvent;
import com.rentalgeek.android.bus.events.MaxResultsReturnedEvent;
import com.rentalgeek.android.bus.events.RefreshFilterDoneLoadingEvent;
import com.rentalgeek.android.bus.events.ShowProfileCreationEvent;
import com.rentalgeek.android.constants.Search;
import com.rentalgeek.android.constants.SharedPrefs;
import com.rentalgeek.android.constants.TabPosition;
import com.rentalgeek.android.logging.AppLogger;
import com.rentalgeek.android.mvp.home.HomePresenter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.mvp.map.MapView;
import com.rentalgeek.android.net.GeekHttpResponseHandler;
import com.rentalgeek.android.net.GlobalFunctions;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.adapter.PageAdapter;
import com.rentalgeek.android.ui.fragment.FragmentMap;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;
import com.rentalgeek.android.ui.preference.AppPreferences;
import com.rentalgeek.android.ui.view.NonSwipeableViewPager;
import com.rentalgeek.android.utils.Analytics;
import com.rentalgeek.android.utils.CosignerInviteCaller;
import com.rentalgeek.android.utils.FilterParams;
import com.rentalgeek.android.utils.ObscuredSharedPreferences;

import static com.rentalgeek.android.constants.IntentKey.DO_SILENT_UPDATE;

public class ActivityHome extends GeekBaseActivity implements Container<ViewPager> {

    public HomePresenter presenter;
    private MapView mapView;
    private RentalListView rentalListView;
    private boolean mapReady = false;
    private Toast maxResultsToast;

    public ActivityHome() {
        super(true, true, true);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_with_tabs);
        inflateStub(R.id.stub, R.layout.pager_container);
        setTabs(true);
        setupNavigation();

        initializeFilterParams();

        maxResultsToast = Toast.makeText(this, "Only showing " + Search.MAX_POSSIBLE_RESULTS + " listings. Zoom in or filter to narrow your search.", Toast.LENGTH_SHORT);
        mapView = new FragmentMap();
        rentalListView = new FragmentRentalListView();

        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.container);

        if (viewPager != null) {
            setupContainer(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        presenter = new HomePresenter();

        if (SessionManager.Instance.getCurrentUser() != null) {
            new CosignerInviteCaller(this, false).fetchCosignerInvites();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean shouldDoSilentUpdate = extras.getBoolean(DO_SILENT_UPDATE, false);
            if (shouldDoSilentUpdate) {
                silentUserDataUpdate();
            }
        }

        disableDrawerGesture();
        Analytics.logUserLogin(this);
    }

    private void initializeFilterParams() {
        FilterParams.INSTANCE.params.put("max_price", Integer.toString(AppPreferences.getSearchMaxPrice()));

        if (AppPreferences.getSelectedManagementCompanyId() != 0) {
            FilterParams.INSTANCE.params.put("property_manager_id", Integer.toString(AppPreferences.getSelectedManagementCompanyId()));
        }

        if (AppPreferences.getSearchBedCount() != SharedPrefs.NO_SELECTION) {
            FilterParams.INSTANCE.params.put("bedrooms_count", Integer.toString(AppPreferences.getSearchBedCount()));
        }

        if (AppPreferences.getSearchBathCount() != SharedPrefs.NO_SELECTION) {
            FilterParams.INSTANCE.params.put("bathrooms_count", Integer.toString(AppPreferences.getSearchBathCount()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        deselectAllMenuItems();
        loadRentals();
    }

    private void loadRentals() {
        if (selectedTab == TabPosition.MAP && mapReady) {
            presenter.getMapRentalOfferings();
        } else if (selectedTab == TabPosition.LIST) {
            presenter.getListRentalOfferings();
        }
    }

    @Override
    public void setupContainer(ViewPager container) {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter.addFragment((FragmentMap) mapView, "Map View");
        adapter.addFragment((FragmentRentalListView) rentalListView, "List View");
        container.setAdapter(adapter);

        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
                AppEventBus.post(new RefreshFilterDoneLoadingEvent());
                loadRentals();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onEventMainThread(MapChangedEvent event) {
        this.mapReady = true;
        presenter.getMapRentalOfferings();
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

    public void onEventMainThread(MaxResultsReturnedEvent event) {
        maxResultsToast.show();
    }

    public void onEventMainThread(LessThanMaxResultsReturnedEvent event) {
        maxResultsToast.cancel();
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
                        AppLogger.log("tagzzz", e);
                    }
                }
            });
    }

}
