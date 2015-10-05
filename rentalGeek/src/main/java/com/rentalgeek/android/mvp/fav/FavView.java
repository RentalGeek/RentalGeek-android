package com.rentalgeek.android.mvp.fav;

import com.rentalgeek.android.pojos.Rental;

public interface FavView {
    public void setRentals(Rental[] rentals);
    public void noFavorites();
}
