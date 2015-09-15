package com.rentalgeek.android.backend;

import java.util.List;

public class UserProfile {

	public List<Profile> profiles;

	public class Profile {
		public String id;
		public String first_name;
		public String last_name;
		public String born_on;
		public String ssn;
		public String drivers_license_number;
		public String drivers_license_state;
		public String phone_number;
		public String bank_name;
		public String bank_city_and_state;
		public String pets_description;
		public String vehicles_description;
		public String was_ever_evicted;
		public String was_ever_evicted_explanation;
		public String is_felon;
		public String is_felon_explanation;
		public String character_reference_name;
		public String character_reference_contact_info;
		public String emergency_contact_name;
		public String emergency_contact_phone_number;
		public String current_home_street_address;
		public String current_home_moved_in_on;
		public String current_home_dissatisfaction_explanation;
		public String current_home_owner;
		public String current_home_owner_contact_info;
		public String previous_home_street_address;
		public String previous_home_moved_in_on;
		public String previous_home_moved_out;
		public String previous_home_dissatisfaction_explanation;
		public String previous_home_owner;
		public String previous_home_owner_contact_info;
		public String employment_status;
		public String current_employment_position;
		public String current_employment_monthly_income;
		public String current_employment_supervisor;
		public String current_employment_employer;
		public String current_employment_employer_phone_number;
		public String current_employment_employer_email_address;
		public String current_employment_started_on;
		public String previous_employment_position;
		public String previous_employment_monthly_income;
		public String previous_employment_supervisor;
		public String previous_employment_employer;
		public String previous_employment_employer_phone_number;
		public String previous_employment_employer_email_address;
		public String previous_employment_started_on;
		public String previous_employment_ended_on;
		public String other_income_monthly_amount;
		public String other_income_sources;
		public String cosigner_name;
		public String cosigner_email_address;
		public String victig_order_id;
		public String victig_url;
		public String desires_to_move_in_on;
		public String roommates_description;
		public String geek_score;
	}

}
