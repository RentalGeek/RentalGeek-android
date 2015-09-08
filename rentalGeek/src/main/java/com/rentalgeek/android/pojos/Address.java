package com.rentalgeek.android.pojos;

/**
 * Created by rajohns on 9/7/15.
 *
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
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateAbbrevation() {
        return stateAbbrevation;
    }

    public void setStateAbbrevation(String stateAbbrevation) {
        this.stateAbbrevation = stateAbbrevation;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddressline2() {
        return getCity() + ", " + getStateAbbrevation() + " " + getZip();
    }

}
