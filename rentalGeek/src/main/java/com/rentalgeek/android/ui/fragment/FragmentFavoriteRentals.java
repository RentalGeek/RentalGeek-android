package com.rentalgeek.android.ui.fragment;

import com.rentalgeek.android.bus.events.RemoveItemEvent;

/**
 * Created by Alan R on 10/20/15.
 */
public class FragmentFavoriteRentals extends FragmentRentalListView {

    public void onEventMainThread(RemoveItemEvent event) {
        int position = event.getPosition();
        removeItem(position);
    }
}
