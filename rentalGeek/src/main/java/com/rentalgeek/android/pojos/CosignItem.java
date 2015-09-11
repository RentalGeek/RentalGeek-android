package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignItem {

    private Address address; //inside rental_offering: myname=jsonname, street=address, city=city, stateAbbreviation=state, zip=zipcode(?int or string)
    private int monthlyCost; //inside rental_offering: monthly_rent_ceiling(int)
    private int numBedrooms; //inside rental_offering: bedroom_count(int)
    private int numBathrooms; //inside rental_offering: full_bathroom_count(int) + half_bathroom_count(int)
    private List<LeaseSigner> signers; //inside roommates[]: full_name, lease_signed_on
    private PropertyContactInfo propertyContactInfo; //inside rental_offering: rental_complex_name, customer_contact_email_address, customer_contact_phone_number(int)
    private String imageUrl; //inside rental_offering: primary_property_photo_url (might not be full url)

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

    public List<LeaseSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<LeaseSigner> signers) {
        this.signers = signers;
    }

    public Spanned getLeaseSignersText() {
        String text = "";

        for (LeaseSigner signer : getSigners()) {
            text += "Lease signed by <b>" + signer.getName() + " </b>" + signer.getDate() + "<br /><br />";
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

    public String getAwaitingSignatureText() {
        return "Awaiting Your Signature";
    }

}
