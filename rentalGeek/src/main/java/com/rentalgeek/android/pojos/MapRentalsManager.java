package com.rentalgeek.android.pojos;

import com.rentalgeek.android.api.ApiManager;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.MapRentalsEvent;
import com.rentalgeek.android.utils.GeekGson;

public class MapRentalsManager extends RentalsManager {

    private static final MapRentalsManager INSTANCE = new MapRentalsManager();

    private MapRentals mapRentals;

    private MapRentalsManager() {

    }

    public static MapRentalsManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void rentalsReceived(String response) {
        mapRentals = GeekGson.getInstance().fromJson(response, MapRentals.class);
        AppEventBus.post(new MapRentalsEvent(mapRentals.all));
    }

    @Override
    protected void sendExistingRentals() {
        AppEventBus.post(new MapRentalsEvent(mapRentals.all));
    }

    @Override
    protected String baseUrl() {
        return ApiManager.loadMapPinData();
    }

    @Override
    protected boolean hasRentalsCached() {
        return mapRentals != null && mapRentals.all != null && mapRentals.all.size() != 0;
    }

}
