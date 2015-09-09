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

    @Override
    public String toString() {
        return String.format("%s: %f %f",id,rental_complex_latitude,rental_complex_longitude);
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
        return headline;
    }

    public int getMonthlyRent() {
        return monthly_rent_floor;
    }
}
