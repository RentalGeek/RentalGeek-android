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
//        applicationItem.setUnsignedLeaseDocumentUrl("https://gradcollege.okstate.edu/sites/default/files/PDF_linking.pdf");

        List<RoommateDTO> roommates2 = new ArrayList<>();
        RoommateDTO roommate21 = new RoommateDTO();
        roommate21.full_name = "Matt Smith";
        roommate21.cosigner_full_name = "Adam Johns";
        roommate21.lease_signed_on = "08/10/2015";
        roommate21.cosigner_lease_signed_on = "08/11/2015";
        RoommateDTO roommate22 = new RoommateDTO();
        roommate22.full_name = "John Johnson";
        roommate22.cosigner_full_name = "Random Guy";
        roommate22.lease_signed_on = "08/11/2015";
        roommate22.cosigner_lease_signed_on = "08/12/2015";
        roommates2.add(roommate21);
        roommates2.add(roommate22);

        PropertyContactInfo propertyContactInfo2 = new PropertyContactInfo("Spring Properties", "info@springproperties.com", "(000) 555-1212");

        Address address2 = new Address("2129 Walnut Dr.", "Manhattan", "KS", "66502");

        ApplicationItem applicationItem2 = new ApplicationItem();
        applicationItem2.setAddress(address2);
        applicationItem2.setMonthlyCost(920);
        applicationItem2.setNumBedrooms(2);
        applicationItem2.setNumBathrooms(1);
        applicationItem2.setRoommates(roommates);
        applicationItem2.setPropertyContactInfo(propertyContactInfo2);
        applicationItem2.setImageUrl("https://s3-us-west-2.amazonaws.com/rental-geek/property-header.jpg");
        applicationItem2.setUnsignedLeaseDocumentUrl("https://gradcollege.okstate.edu/sites/default/files/PDF_linking.pdf");

        List<ApplicationItem> stubbedApplicationItems = new ArrayList<>();
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem);
        stubbedApplicationItems.add(applicationItem2);

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
