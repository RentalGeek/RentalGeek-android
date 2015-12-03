package com.rentalgeek.android.storage;

import com.rentalgeek.android.backend.model.PropertyManager;

import java.util.ArrayList;

public enum PropertyManagementCache {

    INSTANCE;

    public static final int ID_NOT_FOUND = -999;
    public ArrayList<PropertyManager> propertyManagers;

    public int getIdFromName(String name) {
        for (PropertyManager propertyManager : propertyManagers) {
            if (name.equals(propertyManager.name)) {
                return propertyManager.id;
            }
        }

        return ID_NOT_FOUND;
    }

}
