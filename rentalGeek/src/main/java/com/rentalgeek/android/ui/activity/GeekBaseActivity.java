package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.LoginBackend;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;
import com.rentalgeek.android.system.AppSystem;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.dialog.AppProgressDialog;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog;

/**
 * Created by rajohns on 9/1/15.
 * Make sure to call appropriate setup method
 */
public class GeekBaseActivity extends AppCompatActivity {

    private boolean tabbed = true;
    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    //final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);

    protected boolean showSlider;
    protected boolean showActionBar;
    protected boolean authRequired;

    public GeekBaseActivity(boolean showSlider, boolean showActionbar, boolean authRequired) {
        this.showSlider = showSlider;
        this.showActionBar = showActionbar;
        this.authRequired = authRequired;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerMessaging();
        if (authRequired) checkLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterMessaging();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void registerMessaging() {
        AppEventBus.register(this);
    }

    protected void unregisterMessaging() {
        AppEventBus.unregister(this);
    }

    protected void checkLogin() {
        if (!isUserLoggedIn()) {
            Navigation.navigateActivity(this, ActivityLogin.class, true);
        }
    }

    protected boolean isUserLoggedIn() {
        return SessionManager.Instance.getCurrentUser() != null;
    }

    /*
     * Inflates stubview with replacement view.
     * @param stubViewId Id of stub view to be replaced and inflated.
     * @param layoutContainerId Id of layout resource which contains view to
     * replace stub with.
     */
    protected void inflateStub(int stubViewId, int layoutContainerId) {
        ViewStub stub = (ViewStub)findViewById(stubViewId);
        stub.setLayoutResource(layoutContainerId);
        stub.inflate();
    }

    /*
     * Sets whether activity will need to show the tab layout.
     * @param tabbed Flag for tab visibility.
     */
    protected void setTabs(boolean tabbed) {
        this.tabbed = tabbed;
    }

    /*
     * Sets up navigation drawer, toolbar and possibly tab bar.
     * Should be called after inflateStub
     */
    protected void setupNavigation() {
        if ( tabbed ) {
            setupFull();
        }

        else {
            setupPartial();
        }
    }

    /*
     * Set of methods for navigation, toolbar and tab bar setup
     */
    private void setupFull() {
        setupToolbar();
        setupTabs();
        showDrawerMenuIcon();
        setupNavigationView();
    }
    /*
     * Set of methods for navigation and toolbar setup
     */
    private void setupPartial() {
        setupToolbar();
        showDrawerMenuIcon();
        setupNavigationView();
    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if( toolbar != null) {
            if (showActionBar) {
                setSupportActionBar(toolbar);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    private void setupTabs() {
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        if( tabLayout != null ) {
            tabLayout.addTab(tabLayout.newTab().setText("Map View"));
            tabLayout.addTab(tabLayout.newTab().setText("List View"));
        }
    }

    private void showDrawerMenuIcon() {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showProgressDialog(int messageResId) {
        Bundle args = new Bundle();
        args.putInt(Common.DIALOG_MSG_ID, messageResId);
        AppEventBus.post(new AppDialogRequestEvent<AppProgressDialog>(AppProgressDialog.class, args, null, false));
    }

    protected void hideProgressDialog() {
        GeekDialog.dismiss(this, AppProgressDialog.class);
    }

    protected void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (navigationView != null && drawerLayout != null) {

            setVisibilityForCosignerMenuItem();

            if (AppSystem.isV1Build) setVisibilityForV1NavigationMenu();

            if (showSlider) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                setupDrawerListener(navigationView);
            } else {
                //drawerLayout.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                navigationView.setVisibility(View.GONE);
            }
        }
    }

    protected void setupDrawerListener(NavigationView navigationView) {
        final FragmentActivity activity = this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.roommates:
                        Navigation.navigateActivity(activity, ActivityRoommates.class);
                        return true;
                    case R.id.geek_score:
                        LoginBackend.User user = SessionManager.Instance.getCurrentUser();
                        if (user == null) Navigation.navigateActivity(activity, ActivityLogin.class, true);
                        if (user.hasProfileId())
                            Navigation.navigateActivity(activity, ActivityGeekScore.class);
                        else
                            Navigation.navigateActivity(activity, ActivityCreateProfile.class);
                        return true;
                    case R.id.favorites:
                        Navigation.navigateActivity(activity, ActivityFavoriteProperties.class);
                        return true;
                    case R.id.properties:
                        Navigation.navigateActivity(activity, ActivityProperties.class);
                        return true;
                    case R.id.payment:
                        Navigation.navigateActivity(activity, ActivityPayments.class);
                        return true;
                    case R.id.cosigner:
                        Navigation.navigateActivity(activity, ActivityCosignDecider.class);
                        return true;
                    case R.id.logout:
                        SessionManager.Instance.onUserLoggedOut();
                        Navigation.navigateActivity(activity, ActivityLogin.class, true);
                        return true;
                }
                return false;
            }
        });
    }

    public void onEventMainThread(AppDialogRequestEvent<?> event) {
        GeekDialog.AppDialogFragment dialog = GeekDialog.showDialog(this, event.getClazz(), event.getArgs(), event.getCaller());
        if (dialog != null) dialog.setCancelable(event.isCancellable());
    }

    private void setVisibilityForV1NavigationMenu() {
        Menu menu = navigationView.getMenu();

        //Hide menu items for all things in new release to create a v1 play store build
        int[] v2MenuItems = {
                R.id.my_lease,
                R.id.properties,
                R.id.roommates,
                R.id.payment,
                R.id.cosigner,
                R.id.settings
//                R.id.geek_score,
//                R.id.favorites,
//                R.id.geek_vision,
//                R.id.logout
        };

        if (menu != null) {

            for (int i=0; i<v2MenuItems.length; i++) {
                MenuItem menuItem = menu.findItem(v2MenuItems[i]);
                if (menuItem != null) {
                    menuItem.setVisible(false);
                }
            }

        }
    }

    private void setVisibilityForCosignerMenuItem() {
        Menu menu = navigationView.getMenu();

        if (menu != null) {
            MenuItem cosignerItem = menu.findItem(R.id.cosigner);
            if (SessionManager.Instance.getCurrentUser() != null && !SessionManager.Instance.getCurrentUser().is_cosigner) {
                if (cosignerItem != null) {
                    cosignerItem.setVisible(false);
                }
            }
        }
    }

}
