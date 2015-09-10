package com.rentalgeek.android.mvp.rental;

public interface RentalPresenter {
    public void selectStar(String user_id, String rental_id);
    public void unselectStar(String star_id);
}
