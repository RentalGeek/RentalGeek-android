package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class ApplicationItem {

    public static final String SIGN_LEASE = "SIGN LEASE";
    public static final String VIEW_LEASE = "VIEW LEASE";
    public static final String APPROVE = "APPROVE";
    public static final String APPROVED = "APPROVED";

    private Address address;
    private int monthlyCost;
    private int numBedrooms;
    private int numBathrooms;
    protected List<RoommateDTO> roommates;
    private PropertyContactInfo propertyContactInfo;
    private String imageUrl = "https://s3-us-west-2.amazonaws.com/rental-geek/property-header.jpg";
    private String unsignedLeaseDocumentUrl;
    private String signedLeaseUrl;
    private String signedLeaseOn;
    private Integer leaseId;
    private Boolean accepted;
    private Integer rentalOfferingId;
    private Integer userId;

    public ApplicationItem() {
    }

    public ApplicationItem(ApplicationDTO applicationDTO) {
        RentalOfferingDTO rentalOfferingDTO = applicationDTO.rental_offering;
        this.address = new Address(rentalOfferingDTO.address, rentalOfferingDTO.city, rentalOfferingDTO.state, rentalOfferingDTO.zipcode);
        this.monthlyCost = rentalOfferingDTO.monthly_rent_ceiling;
        this.numBedrooms = rentalOfferingDTO.bedroom_count;
        this.numBathrooms = rentalOfferingDTO.full_bathroom_count;
        this.roommates = applicationDTO.roommates;
        this.propertyContactInfo = new PropertyContactInfo(rentalOfferingDTO.rental_complex_name, rentalOfferingDTO.customer_contact_email_address, rentalOfferingDTO.customer_contact_phone_number);
        this.unsignedLeaseDocumentUrl = applicationDTO.unsigned_lease_document_url;
        this.signedLeaseUrl = applicationDTO.signed_lease_url;
        this.leaseId = applicationDTO.lease_id;
        this.signedLeaseOn = applicationDTO.signed_lease_on;
        this.accepted = applicationDTO.accepted;
        this.rentalOfferingId = applicationDTO.rental_offering_id;
        this.userId = applicationDTO.user_id;
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

    public int getNumBathrooms() {
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
        if (signedLeaseOn == null) {
            return SIGN_LEASE;
        }

        return VIEW_LEASE;
    }

    public Spanned getLeftTextForRoomate(RoommateDTO roommate) {
        String text = "";

        if (Boolean.TRUE.equals(accepted)) {
            if (roommate.lease_signed_on != null) {
                return statusLine("Lease signed by", roommate.full_name);
            } else {
                return statusLine("Awaiting signature from ", roommate.full_name);
            }
        } else {
            return statusLine("", roommate.full_name);
        }

    }

    public Spanned getLeftTextForCosigner(RoommateDTO roommate) {
        String text = "";

        if (Boolean.TRUE.equals(accepted)) {
            if (roommate.cosigner_lease_signed_on != null) {
                return statusLine("Lease cosigned by", roommate.cosigner_full_name);
            } else {
                return statusLine("Awaiting cosignature from ", roommate.cosigner_full_name);
            }
        } else {
            return statusLine("Cosigner Approval", "");
        }

    }

    private Spanned statusLine(String leadingText, String nameText) {
        String text = leadingText + "&nbsp;<b>" + getNameText(nameText) + "</b>";
        return Html.fromHtml(text);
    }

    public Spanned getRightTextForRoommate(RoommateDTO roommate) {
        if (Boolean.TRUE.equals(accepted)) {
            if (roommate.lease_signed_on == null) {
                return Html.fromHtml("");
            } else {
                return Html.fromHtml(formattedDate(roommate.lease_signed_on));
            }
        } else {
            return Html.fromHtml(getStatusText(roommate.status));
        }

    }

    public Spanned getRightTextForCosigner(RoommateDTO roommate) {
        if (Boolean.TRUE.equals(accepted)) {
            if (roommate.cosigner_lease_signed_on == null) {
                return Html.fromHtml("");
            } else {
                return Html.fromHtml(formattedDate(roommate.cosigner_lease_signed_on));
            }
        } else {
            return Html.fromHtml(getStatusText(roommate.cosigner_status));
        }

    }

    private String getStatusText(String status) {
        if (status != null) {
            if (status.toLowerCase().equals("applied")) {
                return "<b><font color='#23BC2A'>COMPLETE</font></b> ";
            } else {
                return "<b><font color='#1682CC'>" + status.toUpperCase() + "</font></b> ";
            }
        }

        return "";
    }

    protected String getNameText(String name) {
        if (name == null) {
            return "N/A";
        }

        return name;
    }

    public String formattedDate(String inputDate) {
        return inputDate.substring(0, 10);
    }

    public String getUnsignedLeaseDocumentUrl() {
        return unsignedLeaseDocumentUrl;
    }

    public void setUnsignedLeaseDocumentUrl(String unsignedLeaseDocumentUrl) {
        this.unsignedLeaseDocumentUrl = unsignedLeaseDocumentUrl;
    }

    public String getSignedLeaseUrl() {
        return signedLeaseUrl;
    }

    public void setSignedLeaseUrl(String signedLeaseUrl) {
        this.signedLeaseUrl = signedLeaseUrl;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }

    public String getSignedLeaseOn() {
        return signedLeaseOn;
    }

    public void setSignedLeaseOn(String signedLeaseOn) {
        this.signedLeaseOn = signedLeaseOn;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Integer getRentalOfferingId() {
        return rentalOfferingId;
    }

    public void setRentalOfferingId(Integer rentalOfferingId) {
        this.rentalOfferingId = rentalOfferingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
