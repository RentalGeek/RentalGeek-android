package com.rentalgeek.android.pojos;

import java.util.List;

/**
 * Created by rajohns on 9/7/15.
 *
 */
public class CosignItem {

    private Address address;
    private int monthlyCost;
    private int numBedrooms;
    private int numBathrooms;
    private List<LeaseSigner> signers;
    private PropertyContactInfo propertyContactInfo;

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

    public PropertyContactInfo getPropertyContactInfo() {
        return propertyContactInfo;
    }

    public void setPropertyContactInfo(PropertyContactInfo propertyContactInfo) {
        this.propertyContactInfo = propertyContactInfo;
    }

}
