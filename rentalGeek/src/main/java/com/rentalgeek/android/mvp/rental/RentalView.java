package com.rentalgeek.android.mvp.rental;

import com.rentalgeek.android.pojos.PhotoDTO;
import com.rentalgeek.android.pojos.Rental;

import java.util.ArrayList;

public interface RentalView {
    void hide();
    void show();
    void showRental(Rental rental);
    void showPropertyPhotos(ArrayList<PhotoDTO> propertyPhotos);
    void goToCreateProfile();
}
