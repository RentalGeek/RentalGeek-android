package com.rentalgeek.android.mvp.list.rental;

import com.rentalgeek.android.mvp.common.StarPresenter;
import com.rentalgeek.android.mvp.list.rental.RentalListView;

public class RentalListPresenter extends StarPresenter {
    
    private static final String TAG = RentalListPresenter.class.getSimpleName();
   
    private RentalListView rentalListView;
    
    public RentalListPresenter(RentalListView rentalListView) {
        this.rentalListView = rentalListView;    
    }
}