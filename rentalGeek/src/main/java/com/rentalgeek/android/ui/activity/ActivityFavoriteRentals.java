package com.rentalgeek.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.RemoveItemEvent;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.mvp.fav.FavPresenter;
import com.rentalgeek.android.mvp.fav.FavView;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;

import com.rentalgeek.android.utils.OkAlert;

public class ActivityFavoriteRentals extends GeekBaseActivity implements FavView {

    private static final String TAG = ActivityFavoriteRentals.class.getSimpleName();
    private RentalListView rentalListView;
    private FavPresenter presenter;

    public ActivityFavoriteRentals() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        setupNavigation();
        setMenuItemSelected(R.id.favorites);

        presenter = new FavPresenter(this);

        if (savedInstanceState == null) {
            rentalListView = new FragmentRentalListView();
            Bundle args = getIntent().getExtras();
            ((FragmentRentalListView)rentalListView).setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentRentalListView)rentalListView).commit();
        }
        
        presenter.getFavoriteRentals();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog(R.string.dialog_msg_loading);
        registerMessaging();
        presenter.getFavoriteRentals();
    }

    @Override
    public void onStop() {
        unregisterMessaging();
        super.onStop();
    }

    @Override
    public void setRentals(Rental[] rentals) {
        rentalListView.setRentals(rentals);
        hideProgressDialog();
    }

    @Override
    public void showMessage(String title, String message) {
        hideProgressDialog();
        OkAlert.show(this,title,message);
    }

    public void onEventMainThread(ClickRentalEvent event) {
        
        Bundle bundle = event.getBundle();

        if( bundle != null ) {
            Intent intent = new Intent(this, ActivityRental.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void onEventMainThread(RemoveItemEvent event) {
        int position = event.getPosition();
        rentalListView.removeItem(position);
    }
}
