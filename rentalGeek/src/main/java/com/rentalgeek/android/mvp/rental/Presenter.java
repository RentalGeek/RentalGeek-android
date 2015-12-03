package com.rentalgeek.android.mvp.rental;

public interface Presenter {
    void getRental(String rental_id);

    void getPropertyPhotos(String rental_id);

    void apply(String rental_id);
}
