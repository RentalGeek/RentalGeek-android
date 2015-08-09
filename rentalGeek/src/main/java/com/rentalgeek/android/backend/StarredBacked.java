package com.rentalgeek.android.backend;

import java.util.List;


public class StarredBacked {

	public String error;

	public List<Star> starred_properties;

	public class Star {
		public String id;
		public String applicant_id;
		public String rental_offering_id;
		public String property_address;
		public String bedroom_count;
		public String full_bathroom_count;
		public String square_footage_floor;
		public String monthly_rent_floor;
		public String salesy_description;
		public String sold_out;
		public List<Images> image;
	}

	
	
	public class Images
	{
		public String photo_full_url;
		public boolean primary_photo;
	}
}
