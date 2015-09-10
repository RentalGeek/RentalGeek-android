package com.rentalgeek.android.mvp.rental;

import com.rentalgeek.android.pojos.Rental;

public interface RentalView {
    public void hide();
    public void show();
    public void showRental(Rental rental);
    public void selectStar();
    public void unselectStar();
}
