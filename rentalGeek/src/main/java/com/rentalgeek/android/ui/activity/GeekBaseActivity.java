package com.rentalgeek.android.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.User;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;
import com.rentalgeek.android.bus.events.ErrorAlertEvent;
import com.rentalgeek.android.bus.events.ErrorCroutonEvent;
import com.rentalgeek.android.bus.events.ShowHomeEvent;
import com.rentalgeek.android.system.AppSystem;
import com.rentalgeek.android.ui.Common;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.dialog.AppProgressDialog;
import com.rentalgeek.android.ui.dialog.DialogManager;
import com.rentalgeek.android.ui.dialog.manager.GeekDialog;
import com.rentalgeek.android.utils.CosignerDestinationLogic;

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
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authRequired) checkLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerMessaging();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterMessaging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_map:
                Navigation.navigateActivity(this, ActivityHome.class, false);
                return true;
            case R.id.action_search:
                Navigation.navigateActivity(this, ActivitySearch.class, false);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void disableDrawerGesture() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        ViewStub stub = (ViewStub) findViewById(stubViewId);
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
        if (tabbed) {
            setupFull();
        } else {
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

        if (toolbar != null) {
            if (showActionBar) {
                setSupportActionBar(toolbar);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    private void setupTabs() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText("Map View"));
            tabLayout.addTab(tabLayout.newTab().setText("List View"));
        }
    }

    private void showDrawerMenuIcon() {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.menu_icon);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showProgressDialog(int messageResId) {
        Bundle args = new Bundle();
        args.putInt(Common.DIALOG_MSG_ID, messageResId);
        AppEventBus.post(new AppDialogRequestEvent<AppProgressDialog>(AppProgressDialog.class, args, null, true));
    }

    protected void hideProgressDialog() {
        GeekDialog.dismiss(this, AppProgressDialog.class);
    }

    protected void setupNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (navigationView != null && drawerLayout != null) {
            hideMenuItem(R.id.geek_vision);
            hideMenuItem(R.id.settings);
            setVisibilityForCosignerMenuItem();
            setVisibilityForMyCosignerMenuItem();
            setVisibilityForPaymentsMenuItem();
            setupDrawerListener();

            if (AppSystem.isV1Build) setVisibilityForV1NavigationMenu();

            if (showSlider) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                setupNavViewListener(navigationView);
            } else {
                //drawerLayout.setVisibility(View.GONE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                navigationView.setVisibility(View.GONE);
            }
        }
    }

    private void setupDrawerListener() {
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setVisibilityForCosignerMenuItem();
                setVisibilityForMyCosignerMenuItem();
                setVisibilityForPaymentsMenuItem();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    protected void setupNavViewListener(NavigationView navigationView) {
        final FragmentActivity activity = this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.roommates:
                        Navigation.navigateActivity(activity, ActivityRoommates.class);
                        return true;
                    case R.id.geek_score:
                        User user = SessionManager.Instance.getCurrentUser();
                        if (user == null)
                            Navigation.navigateActivity(activity, ActivityLogin.class, true);
                        if (SessionManager.Instance.hasProfile())
                            Navigation.navigateActivity(activity, ActivityGeekScore.class);
                        else
                            Navigation.navigateActivity(activity, ActivityCreateProfile.class);
                        return true;
                    case R.id.favorites:
                        Navigation.navigateActivity(activity, ActivityFavoriteRentals.class);
                        return true;
                    case R.id.applications:
                        Navigation.navigateActivity(activity, ActivityApplications.class);
                        return true;
                    case R.id.payment:
                        Navigation.navigateActivity(activity, ActivityPayments.class);
                        return true;
                    case R.id.cosigner:
                        decideWhichCosignScreenToShow();
                        return true;
                    case R.id.my_cosigner:
                        Navigation.navigateActivity(activity, ActivityMyCosigner.class);
                        return true;
                    case R.id.settings:
                        Navigation.navigateActivity(activity, ActivitySettings.class);
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
                R.id.applications,
                R.id.roommates,
                R.id.payment,
                R.id.cosigner,
//                R.id.settings
//                R.id.geek_score,
//                R.id.favorites,
//                R.id.geek_vision,
//                R.id.logout
        };

        if (menu != null) {

            for (int i = 0; i < v2MenuItems.length; i++) {
                MenuItem menuItem = menu.findItem(v2MenuItems[i]);
                if (menuItem != null) {
                    menuItem.setVisible(false);
                }
            }

        }
    }

    private void setVisibilityForCosignerMenuItem() {
        if (SessionManager.Instance.getCurrentUser() != null && !SessionManager.Instance.getCurrentUser().is_cosigner) {
            hideMenuItem(R.id.cosigner);
        } else {
            showMenuItem(R.id.cosigner);
        }
    }

    private void setVisibilityForMyCosignerMenuItem() {
        if (SessionManager.Instance.getCurrentUser() == null || SessionManager.Instance.getCurrentUser().profile_id == null) {
            hideMenuItem(R.id.my_cosigner);
        } else {
            showMenuItem(R.id.my_cosigner);
        }
    }

    private void setVisibilityForPaymentsMenuItem() {
        Menu menu = navigationView.getMenu();

        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.payment);
            if (menuItem != null) {
                menuItem.setVisible(SessionManager.Instance.hasCompletedLeaseId());
            }
        }

    }

    protected void hideMenuItem(int menuItemId) {
        Menu menu = navigationView.getMenu();

        if (menu != null) {
            MenuItem menuItem = menu.findItem(menuItemId);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
        }
    }

    protected void showMenuItem(int menuItemId) {
        Menu menu = navigationView.getMenu();

        if (menu != null) {
            MenuItem menuItem = menu.findItem(menuItemId);
            if (menuItem != null) {
                menuItem.setVisible(true);
            }
        }
    }

    private void decideWhichCosignScreenToShow() {
        if (CosignerDestinationLogic.INSTANCE.getCosignerInvites() == null) {
            Navigation.navigateActivity(this, ActivityCosignDecider.class);
        } else {
            CosignerDestinationLogic.INSTANCE.navigateToNextCosignActivity(this);
        }
    }

    protected void setMenuItemSelected(int itemId) {
        Menu menu = navigationView.getMenu();

        if (menu != null) {
            MenuItem itemToSelect = menu.findItem(itemId);
            if (itemToSelect != null) {
                itemToSelect.setChecked(true);
            }
        }
    }

    public void onEventMainThread(ShowHomeEvent event) {
        Navigation.navigateActivity(this, ActivityHome.class, true);
    }

    public void onEventMainThread(ErrorAlertEvent event) {
        final AlertDialog errorDialog = new AlertDialog
                .Builder(this)
                .setTitle(event.getTitle())
                .setMessage(event.getMessage())
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        errorDialog.show();
    }

    public void onEventMainThread(ErrorCroutonEvent event) {
        DialogManager.showCrouton(this,event.getMessage());
    }
}
