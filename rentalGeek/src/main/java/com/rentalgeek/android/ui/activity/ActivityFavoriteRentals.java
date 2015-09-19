package com.rentalgeek.android.ui.activity;

import android.os.Bundle;
import com.rentalgeek.android.R;
import android.app.ProgressDialog;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.fav.FavView;
import com.rentalgeek.android.mvp.fav.FavPresenter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;
import com.rentalgeek.android.ui.fragment.FragmentRentalListView;

public class ActivityFavoriteRentals extends GeekBaseActivity implements FavView {

    private static final String TAG = ActivityFavoriteRentals.class.getSimpleName();
    private RentalListView rentalListView;
    private FavPresenter presenter;
    private ProgressDialog loading;

    public ActivityFavoriteRentals() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        setupNavigation();

        presenter = new FavPresenter(this);

        if (savedInstanceState == null) {
            rentalListView = new FragmentRentalListView();
            Bundle args = getIntent().getExtras();
            ((FragmentRentalListView)rentalListView).setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, (FragmentRentalListView)rentalListView).commit();
        }
        
        //TODO, rewrite dialog got GeekAbstract Fragment
        loading = new ProgressDialog(this);
        loading.setMessage("Loading...");
        loading.show();
        presenter.getFavoriteRentals();
    }

    @Override
    public void setRentals(Rental[] rentals) {
        rentalListView.setRentals(rentals);
        loading.dismiss();
    }

}
