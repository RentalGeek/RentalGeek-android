package com.rentalgeek.android.pojos;

/**
 * Created by rajohns on 9/7/15.
 */
public class Address {

    private String street;
    private String city;
    private String stateAbbrevation;
    private String zip;

    public Address(String street, String city, String stateAbbrevation, String zip) {
        this.street = street;
        this.city = city;
        this.stateAbbrevation = stateAbbrevation;
        this.zip = zip;
    }

    public String getStreet() {
        if (street == null) {
            return "Street: N/A";
        }

        return street;
    }

    public String getCity() {
        if (city == null) {
            return "City: N/A";
        }

        return city;
    }

    public String getStateAbbrevation() {
        if (stateAbbrevation == null) {
            return "State: N/A";
        }

        return stateAbbrevation;
    }

    public String getZip() {
        if (zip == null) {
            return "Zip: N/A";
        }

        return zip;
    }

    public String getAddressline2() {
        return getCity() + ", " + getStateAbbrevation() + " " + getZip();
    }

}
