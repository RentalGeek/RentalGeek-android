package com.rentalgeek.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.NoFavoritesEvent;
import com.rentalgeek.android.bus.events.SetRentalsEvent;
import com.rentalgeek.android.mvp.fav.FavPresenter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.ui.fragment.FragmentFavoriteRentals;
import com.rentalgeek.android.ui.fragment.FragmentNoFavorites;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;

public class ActivityFavoriteRentals extends GeekBaseActivity {

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

        presenter = new FavPresenter();

        if (savedInstanceState == null) {
            rentalListView = new FragmentFavoriteRentals();
            Bundle args = getIntent().getExtras();
            ((FragmentRentalListView) rentalListView).setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentRentalListView) rentalListView).commit();
        }

        presenter.getFavoriteRentals();
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressDialog(R.string.dialog_msg_loading);
        presenter.getFavoriteRentals();
    }

    public void onEventMainThread(SetRentalsEvent event) {
        if (event.getRentals() != null) {
            rentalListView.setRentals(event.getRentals());
            hideProgressDialog();
        }
    }

    public void onEventMainThread(NoFavoritesEvent event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentNoFavorites()).commit();
        hideProgressDialog();
    }

    public void onEventMainThread(ClickRentalEvent event) {
        Bundle bundle = event.getBundle();

        if (bundle != null) {
            Intent intent = new Intent(this, ActivityRental.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
