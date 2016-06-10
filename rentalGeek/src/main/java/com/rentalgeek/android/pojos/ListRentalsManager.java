package com.rentalgeek.android.pojos;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.LessThanMaxResultsReturnedEvent;
import com.rentalgeek.android.bus.events.ListRentalsEvent;
import com.rentalgeek.android.bus.events.MaxResultsReturnedEvent;
import com.rentalgeek.android.constants.Search;
import com.rentalgeek.android.utils.GeekGson;

public class ListRentalsManager extends RentalsManager {

    private static final ListRentalsManager INSTANCE = new ListRentalsManager();

    private ListRentals listRentals;

    private ListRentalsManager() {

    }

    public static ListRentalsManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void rentalsReceived(String response) {
        listRentals = GeekGson.getInstance().fromJson(response, ListRentals.class);
        if (listRentals.all.size() == Search.MAX_POSSIBLE_RESULTS) {
            AppEventBus.post(new MaxResultsReturnedEvent());
        } else {
            AppEventBus.post(new LessThanMaxResultsReturnedEvent());
        }
        sendExistingRentals();
    }

    @Override
    protected void sendExistingRentals() {
        AppEventBus.post(new ListRentalsEvent(listRentals.all));
    }

    @Override
    protected String baseUrl() {
        return ApiManager.loadRentalListData();
    }

    @Override
    protected boolean hasRentalsCached() {
        return listRentals != null && listRentals.all != null && listRentals.all.size() != 0;
    }

    public ListRental get(String rentalId) {
        if (listRentals != null) {
            for (ListRental rental : listRentals.all) {
                if (rental.id == Integer.parseInt(rentalId)) {
                    return rental;
                }
            }
        }

        return null;
    }

}
