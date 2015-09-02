package com.rentalgeek.android.pojos;

public class Rental {
		
    public int id;
	public double rental_complex_latitude;
	public double rental_complex_longitude;
	public boolean starred;
	public int bedroom_count;
	public int monthly_rent_floor;
	public int monthly_rent_ceiling;
	public String headline;
	public int full_bathroom_count;
	public String primary_property_photo_url;

    @Override
    public String toString() {
        return String.format("%s: %f %f",id,rental_complex_latitude,rental_complex_longitude);
    }

    public double getLatitude() {
        return rental_complex_latitude;
    }

    public double getLongitude() {
        return rental_complex_longitude;
    }
}
