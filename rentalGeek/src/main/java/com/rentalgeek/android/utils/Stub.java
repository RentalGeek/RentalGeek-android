package com.rentalgeek.android.utils;

import com.rentalgeek.android.pojos.Address;
import com.rentalgeek.android.pojos.ApplicationItem;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.pojos.PropertyContactInfo;
import com.rentalgeek.android.pojos.RoommateDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/10/15.
 *
 */
public class Stub {

    public static List<ApplicationItem> cosignProperties() {
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
        applicationItem.setImageUrl("https://s3-us-west-2.amazonaws.com/rental-geek/property-header.jpg");

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

    public static ArrayList<CosignerInviteDTO> cosignerInvites() {
        CosignerInviteDTO cosignerInviteDTO = new CosignerInviteDTO();
        cosignerInviteDTO.accepted = Boolean.TRUE;
        cosignerInviteDTO.id = 1234;
        cosignerInviteDTO.inviter_id = 45;
        cosignerInviteDTO.inviter_name = "John Smith";

        ArrayList<CosignerInviteDTO> stubbedCosignerInvites = new ArrayList<>();
        stubbedCosignerInvites.add(cosignerInviteDTO);
        stubbedCosignerInvites.add(cosignerInviteDTO);
        stubbedCosignerInvites.add(cosignerInviteDTO);
        stubbedCosignerInvites.add(cosignerInviteDTO);
        stubbedCosignerInvites.add(cosignerInviteDTO);

        return stubbedCosignerInvites;
    }

}
