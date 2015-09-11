package com.rentalgeek.android.utils;

import com.rentalgeek.android.pojos.Address;
import com.rentalgeek.android.pojos.CosignItem;
import com.rentalgeek.android.pojos.PropertyContactInfo;
import com.rentalgeek.android.pojos.Roommate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/10/15.
 *
 */
public class Stub {

    public static List<CosignItem> cosignItems() {
        List<Roommate> roommates = new ArrayList<>();
        Roommate roommate1 = new Roommate();
        roommate1.full_name = "Matt Smith";
        roommate1.lease_signed_on = "08/10/2015";
        Roommate roommate2 = new Roommate();
        roommate2.full_name = "John Johnson";
        roommate2.lease_signed_on = "08/11/2015";
        roommates.add(roommate1);
        roommates.add(roommate2);

        PropertyContactInfo propertyContactInfo = new PropertyContactInfo("Spring Properties", "info@springproperties.com", "(000) 555-1212");

        Address address = new Address("2129 Walnut Dr.", "Manhattan", "KS", "66502");

        CosignItem cosignItem = new CosignItem();
        cosignItem.setAddress(address);
        cosignItem.setMonthlyCost(920);
        cosignItem.setNumBedrooms(2);
        cosignItem.setNumBathrooms(1);
        cosignItem.setRoommates(roommates);
        cosignItem.setPropertyContactInfo(propertyContactInfo);
        cosignItem.setImageUrl("https://rental-geek.s3.amazonaws.com/uploads/property_photo/photo/2155/large_IMG_3955.JPG");

        List<CosignItem> stubbedCosignItems = new ArrayList<>();
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);
        stubbedCosignItems.add(cosignItem);

        return stubbedCosignItems;
    }

}
