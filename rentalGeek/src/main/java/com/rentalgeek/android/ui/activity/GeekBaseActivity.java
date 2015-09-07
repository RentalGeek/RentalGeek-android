package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewStub;
import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.Common;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.GravityCompat;
import com.rentalgeek.android.bus.AppEventBus;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import com.rentalgeek.android.ui.dialog.AppProgressDialog;
import com.rentalgeek.android.bus.events.AppDialogRequestEvent;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        if( toolbar != null ) {
            setSupportActionBar(toolbar);
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

    protected void setupNavigationView() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.navigationView);
        if (navigationView != null && drawerLayout != null) {
            setupDrawerListener(navigationView);
        }
    }

    protected void setupDrawerListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}
