package com.rentalgeek.android.mvp.rental;

public interface Presenter {
    public void selectStar(String user_id, String rental_id);
    public void unselectStar(String star_id);
}
