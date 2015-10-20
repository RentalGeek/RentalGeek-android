package com.rentalgeek.android.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajohns on 9/10/15.
 */
public class RentalOfferingDTO {

    public Integer id;
    public Integer bedroom_count;
    public Integer applications_count;
    public Integer full_bathroom_count;
    public Integer half_bathroom_count;
    public Integer monthly_rent_floor;
    public Integer monthly_rent_ceiling;
    public Integer square_footage_floor;
    public Integer square_footage_ceiling;
    public String url;
    public String headline;
    public String rental_offering_type;
    public String customer_contact_email_address;
    public String customer_contact_phone_number;
    public String salesy_description;
    public String earliest_available_on;
    public Boolean buzzer_intercom;
    public Boolean central_air;
    public Boolean deck_patio;
    public Boolean dishwasher;
    public Boolean doorman;
    public Boolean elevator;
    public Boolean fireplace;
    public Boolean gym;
    public Boolean hardwood_floor;
    public Boolean new_appliances;
    public Boolean parking_garage;
    public Boolean parking_outdoor;
    public Boolean pool;
    public Boolean storage_space;
    public Boolean vaulted_ceiling;
    public Boolean walkin_closet;
    public Boolean washer_dryer;
    public Boolean yard_private;
    public Boolean yard_shared;
    public List<String> scrape_amenities;
    public Boolean starred;
    public Integer starred_property_id;
    public String primary_property_photo_url;
    public Integer rental_complex_id;
    public String address;
    public String city;
    public String state;
    public String zipcode;
    public Integer property_manager_id;
    public Boolean already_applied;
    public Double rental_complex_latitude;
    public Double rental_complex_longitude;
    public String rental_complex_name;
    public String rental_complex_full_address;
    public String rental_complex_street_name;
    public String rental_complex_cross_street_name;
    public String rental_complex_walk_time;
    public ArrayList<String> rental_complex_nearby_places;
    public Boolean property_manager_accepts_cash;
    public Boolean property_manager_accepts_checks;
    public Boolean property_manager_accepts_credit_cards_offline;
    public Boolean property_manager_accepts_online_payments;
    public Boolean property_manager_accepts_money_orders;

}
