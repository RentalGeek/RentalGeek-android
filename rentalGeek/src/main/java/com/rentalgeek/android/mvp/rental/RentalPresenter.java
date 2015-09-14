package com.rentalgeek.android.mvp.rental;

import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.mvp.common.StarPresenter;

public class RentalPresenter extends StarPresenter {
    
    private static final String TAG = RentalPresenter.class.getSimpleName();
   
    private RentalView rentalView;
    
    public RentalPresenter(RentalView rentalView) {
        this.rentalView = rentalView;    
    }
}
