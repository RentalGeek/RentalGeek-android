package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignItem {

    private Address address;
    private int monthlyCost;
    private int numBedrooms;
    private double numBathrooms;
    private List<Roommate> roommates;
    private PropertyContactInfo propertyContactInfo;
    private String imageUrl = "https://s3-us-west-2.amazonaws.com/rental-geek/property-header.jpg";

    public CosignItem() {
    }

    public CosignItem(Application application) {
        RentalOffering rentalOffering = application.rental_offering;
        this.address = new Address(rentalOffering.address, rentalOffering.city, rentalOffering.state, rentalOffering.zipcode);
        this.monthlyCost = rentalOffering.monthly_rent_ceiling;
        this.numBedrooms = rentalOffering.bedroom_count;
        this.numBathrooms = rentalOffering.full_bathroom_count + rentalOffering.half_bathroom_count/2;
        this.roommates = application.roommates; //change to application.cosignee_roomates after that field starts coming down
        this.propertyContactInfo = new PropertyContactInfo(rentalOffering.rental_complex_name, rentalOffering.customer_contact_email_address, rentalOffering.customer_contact_phone_number);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(int monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public String getMonthlyCostText() {
        return "$" + getMonthlyCost();
    }

    public int getNumBedrooms() {
        return numBedrooms;
    }

    public void setNumBedrooms(int numBedrooms) {
        this.numBedrooms = numBedrooms;
    }

    public double getNumBathrooms() {
        return numBathrooms;
    }

    public String getNumBedBathText() {
        return getNumBedrooms() + " BR, " + getNumBathrooms() + " Bath";
    }

    public void setNumBathrooms(int numBathrooms) {
        this.numBathrooms = numBathrooms;
    }

    public List<Roommate> getRoommates() {
        return roommates;
    }

    public void setRoommates(List<Roommate> roommates) {
        this.roommates = roommates;
    }

    public Spanned getLeaseSignersText() {
        String text = "";

        for (Roommate roommate : getRoommates()) {
            if (roommate.lease_signed_on != null) {
                text += "Lease signed by <b>" + roommate.full_name + " </b>" + roommate.lease_signed_on + "<br /><br />";
            } else {
                text += "Awaiting signature from <b>" + roommate.full_name + " </b><br /><br />";
            }

            if (roommate.cosigner_lease_signed_on == null /*&& roommate.is_my_cosignee == Boolean.TRUE*/) {
                text += "<font color='red'>Awaiting Your Signature</font><br /><br />";
            }
        }

        return Html.fromHtml(text);
    }

    public PropertyContactInfo getPropertyContactInfo() {
        return propertyContactInfo;
    }

    public void setPropertyContactInfo(PropertyContactInfo propertyContactInfo) {
        this.propertyContactInfo = propertyContactInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getButtonText() {
        return "SIGN LEASE";
    }

}
