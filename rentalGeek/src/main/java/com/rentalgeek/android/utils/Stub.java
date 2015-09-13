package com.rentalgeek.android.utils;

import com.rentalgeek.android.pojos.Address;
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.pojos.PropertyContactInfo;
import com.rentalgeek.android.pojos.RoommateDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/10/15.
 *
 */
public class Stub {

    public static List<ApplicationItem> cosignItems() {
        List<RoommateDTO> roommates = new ArrayList<>();
        RoommateDTO roommate1 = new RoommateDTO();
        roommate1.full_name = "Matt Smith";
        roommate1.cosigner_full_name = "Adam Johns";
        roommate1.lease_signed_on = "08/10/2015";
        roommate1.cosigner_lease_signed_on = "08/11/2015";
        RoommateDTO roommate2 = new RoommateDTO();
        roommate2.full_name = "John Johnson";
        roommate2.cosigner_full_name = "Random Guy";
        roommate2.lease_signed_on = "08/11/2015";
        roommate2.cosigner_lease_signed_on = "08/12/2015";
        roommates.add(roommate1);
        roommates.add(roommate2);

        PropertyContactInfo propertyContactInfo = new PropertyContactInfo("Spring Properties", "info@springproperties.com", "(000) 555-1212");

        Address address = new Address("2129 Walnut Dr.", "Manhattan", "KS", "66502");

        ApplicationItem applicationItem = new ApplicationItem();
        applicationItem.setAddress(address);
        applicationItem.setMonthlyCost(920);
        applicationItem.setNumBedrooms(2);
        applicationItem.setNumBathrooms(1);
        applicationItem.setRoommates(roommates);
        applicationItem.setPropertyContactInfo(propertyContactInfo);
        applicationItem.setImageUrl("https://rental-geek.s3.amazonaws.com/uploads/property_photo/photo/2155/large_IMG_3955.JPG");

        List<ApplicationItem> stubbedApplicationItems = new ArrayList<>();
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);

        return stubbedApplicationItems;
    }

}
