package com.app.rentalgeek.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 
 * @author George
 * 
 * @purpose Database class which saves the rental offerings from API to the Local DB
 *
 */

@Table(name = "PropertyTable")
public class PropertyTable extends Model {

	@Column(name = "count")
	public int count;
	
	@Column(name = "uid")
	public int uid;

	@Column(name = "rental_complex_latitude")
	public Double rental_complex_latitude;

	@Column(name = "rental_complex_longitude")
	public Double rental_complex_longitude;

	@Column(name = "bedroom_count")
	public int bedroom_count;

	@Column(name = "monthly_rent_floor")
	public int monthly_rent_floor;

	@Column(name = "monthly_rent_ceiling")
	public int monthly_rent_ceiling;

	@Column(name = "headline")
	public String headline;

	@Column(name = "full_bathroom_count")
	public int full_bathroom_count;
	
	@Column(name = "rental_offering_type")
	public String rental_offering_type;
	
	@Column(name = "customer_contact_email_address")
	public String customer_contact_email_address;
	
	@Column(name = "rental_complex_name")
	public String rental_complex_name;
	
	@Column(name = "rental_complex_full_address")
	public String rental_complex_full_address;
	
	@Column(name = "rental_complex_street_name")
	public String rental_complex_street_name;
	
	@Column(name = "rental_complex_cross_street_name")
	public String rental_complex_cross_street_name;
	
	@Column(name = "starred")
	public boolean starred;
	
	@Column(name = "buzzer_intercom")
	public boolean buzzer_intercom;
	
	@Column(name = "central_air")
	public boolean central_air;
	
	@Column(name = "deck_patio")
	public boolean deck_patio;
	
	@Column(name = "dishwasher")
	public boolean dishwasher;
	
	@Column(name = "doorman")
	public boolean doorman;
	
	@Column(name = "elevator")
	public boolean elevator;
	
	@Column(name = "fireplace")
	public boolean fireplace;
	
	@Column(name = "gym")
	public boolean gym;
	
	@Column(name = "hardwood_floor")
	public boolean hardwood_floor;
	
	@Column(name = "new_appliances")
	public boolean new_appliances;
	
	@Column(name = "parking_garage")
	public boolean parking_garage;
	
	@Column(name = "parking_outdoor")
	public boolean parking_outdoor;
	
	@Column(name = "pool")
	public boolean pool;
	
	@Column(name = "storage_space")
	public boolean storage_space;
	
	@Column(name = "vaulted_ceiling")
	public boolean vaulted_ceiling;
	
	@Column(name = "walkin_closet")
	public boolean walkin_closet;
	
	@Column(name = "washer_dryer")
	public boolean washer_dryer;
	
	@Column(name = "yard_private")
	public boolean yard_private;
	
	@Column(name = "yard_shared")
	public boolean yard_shared;
	
	@Column(name = "property_manager_accepts_cash")
	public boolean property_manager_accepts_cash;
	
	@Column(name = "property_manager_accepts_checks")
	public boolean property_manager_accepts_checks;
	
	
	@Column(name = "property_manager_accepts_credit_cards_offline")
	public boolean property_manager_accepts_credit_cards_offline;
	
	@Column(name = "property_manager_accepts_online_payments")
	public boolean property_manager_accepts_online_payments;
	
	@Column(name = "property_manager_accepts_money_orders")
	public boolean property_manager_accepts_money_orders;
	
	@Column(name = "property_image")
	public String property_image;
	
	@Column(name = "salesy_description")
	public String salesy_description;
	
	@Column(name = "starred_property_id")
	public String starred_property_id;
	
	
	
	

	public PropertyTable(int count, int uid, Double rental_complex_latitude,
			Double rental_complex_longitude, int bedroom_count,
			int monthly_rent_floor, int monthly_rent_ceiling, String headline,
			int full_bathroom_count, String rental_offering_type,
			String customer_contact_email_address, String rental_complex_name,
			String rental_complex_full_address,
			String rental_complex_street_name,
			String rental_complex_cross_street_name, boolean starred,
			boolean buzzer_intercom, boolean central_air, boolean deck_patio,
			boolean dishwasher, boolean doorman, boolean elevator,
			boolean fireplace, boolean gym, boolean hardwood_floor,
			boolean new_appliances, boolean parking_garage,
			boolean parking_outdoor, boolean pool, boolean storage_space,
			boolean vaulted_ceiling, boolean walkin_closet,
			boolean washer_dryer, boolean yard_private, boolean yard_shared,
			boolean property_manager_accepts_cash,
			boolean property_manager_accepts_checks,
			boolean property_manager_accepts_credit_cards_offline,
			boolean property_manager_accepts_online_payments,
			boolean property_manager_accepts_money_orders,
			String property_image,String salesy_description,String starred_property_id) {
		super();
		this.count = count;
		this.uid = uid;
		this.rental_complex_latitude = rental_complex_latitude;
		this.rental_complex_longitude = rental_complex_longitude;
		this.bedroom_count = bedroom_count;
		this.monthly_rent_floor = monthly_rent_floor;
		this.monthly_rent_ceiling = monthly_rent_ceiling;
		this.headline = headline;
		this.full_bathroom_count = full_bathroom_count;
		this.rental_offering_type = rental_offering_type;
		this.customer_contact_email_address = customer_contact_email_address;
		this.rental_complex_name = rental_complex_name;
		this.rental_complex_full_address = rental_complex_full_address;
		this.rental_complex_street_name = rental_complex_street_name;
		this.rental_complex_cross_street_name = rental_complex_cross_street_name;
		this.starred = starred;
		this.buzzer_intercom = buzzer_intercom;
		this.central_air = central_air;
		this.deck_patio = deck_patio;
		this.dishwasher = dishwasher;
		this.doorman = doorman;
		this.elevator = elevator;
		this.fireplace = fireplace;
		this.gym = gym;
		this.hardwood_floor = hardwood_floor;
		this.new_appliances = new_appliances;
		this.parking_garage = parking_garage;
		this.parking_outdoor = parking_outdoor;
		this.pool = pool;
		this.storage_space = storage_space;
		this.vaulted_ceiling = vaulted_ceiling;
		this.walkin_closet = walkin_closet;
		this.washer_dryer = washer_dryer;
		this.yard_private = yard_private;
		this.yard_shared = yard_shared;
		this.property_manager_accepts_cash = property_manager_accepts_cash;
		this.property_manager_accepts_checks = property_manager_accepts_checks;
		this.property_manager_accepts_credit_cards_offline = property_manager_accepts_credit_cards_offline;
		this.property_manager_accepts_online_payments = property_manager_accepts_online_payments;
		this.property_manager_accepts_money_orders = property_manager_accepts_money_orders;
		this.property_image = property_image;
		this.salesy_description=salesy_description;
		this.starred_property_id=starred_property_id;
	}



	public PropertyTable() {
		super();
	}

}
