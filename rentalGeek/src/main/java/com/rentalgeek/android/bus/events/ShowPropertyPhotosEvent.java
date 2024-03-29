package com.rentalgeek.android.bus.events;

import com.rentalgeek.android.pojos.PhotoDTO;

import java.util.ArrayList;

public class ShowPropertyPhotosEvent {

    private ArrayList<PhotoDTO> propertyPhotos;

    public ShowPropertyPhotosEvent(ArrayList<PhotoDTO> propertyPhotos) {
        this.propertyPhotos = propertyPhotos;
    }

    public ArrayList<PhotoDTO> getPropertyPhotos() {
        return this.propertyPhotos;
    }

}
