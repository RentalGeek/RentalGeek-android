package com.rentalgeek.android.pojos;

public class Rental {
		
    private String id;
	private double rental_complex_latitude;
	private double rental_complex_longitude;
	private boolean starred;
	private int bedroom_count;
	private int monthly_rent_floor;
	private int monthly_rent_ceiling;
	private String headline;
	private int full_bathroom_count;
	private String primary_property_photo_url;
    private String star_id;
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String salesy_description;
    private String[] scrape_amenities;

    public String toString() {
        return String.format("id: %s, starred: %s",id,starred);
    }

    public String getId() {
        return id;
    }

    public double getLatitude() {
        return rental_complex_latitude;
    }

    public double getLongitude() {
        return rental_complex_longitude;
    }

    public int getBedroomCount() {
        return bedroom_count;
    }

    public String getHeadline() {
        return (headline != null) ? headline : "N/A";
    }

    public int getMonthlyRent() {
        return monthly_rent_floor;
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
        if( star_id != null && !star_id.isEmpty() ) {
            this.star_id = star_id;
        }
    }

    public String getStarId() {
        return star_id;
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
        return salesy_description;
    }

    public String[] getAmenities() {
        return scrape_amenities;
    }
}
