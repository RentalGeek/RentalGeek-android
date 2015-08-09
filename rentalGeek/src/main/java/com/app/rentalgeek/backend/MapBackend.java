package com.app.rentalgeek.backend;

import java.util.List;


public class MapBackend {

	public String error;

	public List<Offer> rental_offerings;

	public class Offer {
		public int id;
		public int bedroom_count;
		public String applies_count;
		public int full_bathroom_count;
		public String half_bathroom_count;
		public int monthly_rent_floor;
		public int monthly_rent_ceiling;
		public String square_footage_floor;
		public String square_footage_ceiling;
		public String url;
		public String headline;
		public String earliest_available_on;
		public String rental_offering_type;
		public String customer_contact_email_address;
		public String customer_contact_phone_number;
		public String salesy_description;
		public boolean buzzer_intercom;
		public boolean central_air;
		public boolean deck_patio;
		public boolean dishwasher;
		public boolean doorman;
		public boolean elevator;
		public boolean fireplace;
		public boolean gym;
		public boolean hardwood_floor;
		public boolean new_appliances;
		public boolean parking_garage;
		public boolean parking_outdoor;
		public boolean pool;
		public boolean storage_space;
		public boolean vaulted_ceiling;
		public boolean walkin_closet;
		public boolean washer_dryer;
		public boolean yard_private;
		public boolean yard_shared;
		public boolean starred;
		public String starred_property_id;
		// public String primary_property_photo_url;
		public int rental_complex_id;
		public Double rental_complex_latitude;
		public Double rental_complex_longitude;
		public String rental_complex_name;
		public String rental_complex_full_address;
		public String rental_complex_street_name;
		public String rental_complex_cross_street_name;
		public String rental_complex_walk_time;
		public boolean property_manager_accepts_cash;
		public boolean property_manager_accepts_checks;
		public boolean property_manager_accepts_credit_cards_offline;
		public boolean property_manager_accepts_online_payments;
		public boolean property_manager_accepts_money_orders;
		public List<Photo> primary_property_photo_url;

		public class Photo {
			public String photo_thumb_url;
			public String photo_full_url;
			public boolean primary_photo;
		}
	}

}
