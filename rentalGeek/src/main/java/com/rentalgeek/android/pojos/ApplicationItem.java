package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ApplicationItem {

    private Address address;
    private int monthlyCost;
    private int numBedrooms;
    private double numBathrooms;
    protected List<RoommateDTO> roommates;
    private PropertyContactInfo propertyContactInfo;
    private String imageUrl = "https://s3-us-west-2.amazonaws.com/rental-geek/property-header.jpg";
    private String unsignedLeaseDocumentUrl;

    public ApplicationItem() {
    }

    public ApplicationItem(ApplicationDTO applicationDTO) {
        RentalOfferingDTO rentalOfferingDTO = applicationDTO.rental_offering;
        this.address = new Address(rentalOfferingDTO.address, rentalOfferingDTO.city, rentalOfferingDTO.state, rentalOfferingDTO.zipcode);
        this.monthlyCost = rentalOfferingDTO.monthly_rent_ceiling;
        this.numBedrooms = rentalOfferingDTO.bedroom_count;
        this.numBathrooms = rentalOfferingDTO.full_bathroom_count + rentalOfferingDTO.half_bathroom_count/2;
        this.roommates = applicationDTO.roommates;
        this.propertyContactInfo = new PropertyContactInfo(rentalOfferingDTO.rental_complex_name, rentalOfferingDTO.customer_contact_email_address, rentalOfferingDTO.customer_contact_phone_number);
        this.unsignedLeaseDocumentUrl = applicationDTO.unsigned_lease_document_url;
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

    public List<RoommateDTO> getRoommates() {
        return roommates;
    }

    public void setRoommates(List<RoommateDTO> roommates) {
        this.roommates = roommates;
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

    public Spanned getLeftTextForRoomate(RoommateDTO roommate) {
        String text = "";
        if (roommate.lease_signed_on != null) {
            text += "Lease signed by   <b>" + getNameText(roommate.full_name) + "</b>";
        } else {
            text += "Awaiting signature from   <b>" + getNameText(roommate.full_name) + "</b>";
        }

        text = text.replace("  ", "&nbsp;&nbsp;");
        return Html.fromHtml(text);
    }

    public Spanned getLeftTextForCosigner(RoommateDTO roommate) {
        String text = "";
        if (roommate.cosigner_lease_signed_on != null) {
            text += "Lease cosigned by   <b>" + getNameText(roommate.cosigner_full_name) + "</b>";
        } else {
            text += "Awaiting cosignature from   <b>" + getNameText(roommate.cosigner_full_name) + "</b>";
        }

        text = text.replace("  ", "&nbsp;&nbsp;");
        return Html.fromHtml(text);
    }

    public Spanned getRightTextForRoommate(RoommateDTO roommate) {
        if (roommate.lease_signed_on == null) {
            return Html.fromHtml("");
        }

        return Html.fromHtml(roommate.lease_signed_on);
    }

    public Spanned getRightTextForCosigner(RoommateDTO roommate) {
        if (roommate.cosigner_lease_signed_on == null) {
            return Html.fromHtml("");
        }

        return Html.fromHtml(roommate.cosigner_lease_signed_on);
    }

    protected String getNameText(String name) {
        if (name == null) {
            return "N/A";
        }

        return name;
    }

    public String getUnsignedLeaseDocumentUrl() {
        return unsignedLeaseDocumentUrl;
    }

    public void setUnsignedLeaseDocumentUrl(String unsignedLeaseDocumentUrl) {
        this.unsignedLeaseDocumentUrl = unsignedLeaseDocumentUrl;
    }

}
