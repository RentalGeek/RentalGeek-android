package com.rentalgeek.android.pojos;

// TODO: MOVE ALL USAGES OF THIS OVER TO RENTALDETAIL

public class Rental {

    private String id;
    private double latitude;
    private double longitude;
    private boolean starred;
    private int bedroom_count;
    private int monthly_rent_ceiling;
    private int full_bathroom_count;
    private String primary_property_photo_url;
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String salesy_description;
    private String[] scrape_amenities;
    private String starred_property_id;
    private boolean already_applied;

    public String toString() {
        return String.format("id: %s, starred: %s", id, starred);
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getBedroomCount() {
        return bedroom_count;
    }

    public int getMonthlyRent() {
        return monthly_rent_ceiling;
    }

    public String getImageUrl() {
        return primary_property_photo_url;
    }

    public int getBathroomCount() {
        return full_bathroom_count;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setStarId(String star_id) {
        if (star_id != null && !star_id.isEmpty()) {
            starred_property_id = star_id;
        }
    }

    public String getStarId() {
        return starred_property_id;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getDescription() {
        if (salesy_description == null) {
            return "No description available";
        }

        return salesy_description;
    }

    public String[] getAmenities() {
        return scrape_amenities;
    }

    public boolean applied() {
        return already_applied;
    }

}
