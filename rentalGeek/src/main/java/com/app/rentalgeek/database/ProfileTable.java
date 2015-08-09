package com.app.rentalgeek.database;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ProfileTable")
public class ProfileTable extends Model{
	
	
	@Column(name = "uid")
	public String uid;
	
	@Column(name = "firstname")
	public String firstname;
	
	@Column(name = "lastname")
	public String lastname;
	
	@Column(name = "born_on")
	public String born_on;
	
	@Column(name = "drivers_license_number")
	public String drivers_license_number;
	
	@Column(name = "drivers_license_state")
	public String drivers_license_state;
	
	@Column(name = "phone_number")
	public String phone_number;
	
	@Column(name = "pets_description")
	public String pets_description;
	
	@Column(name = "vehicles_description")
	public String vehicles_description;
	
	@Column(name = "was_ever_evicted")
	public String was_ever_evicted;
	
	@Column(name = "was_ever_evicted_explanation")
	public String was_ever_evicted_explanation;
	
	@Column(name = "is_felon")
	public String is_felon;
	
	@Column(name = "is_felon_explanation")
	public String is_felon_explanation;
	
	@Column(name = "character_reference_name")
	public String character_reference_name;
	
	@Column(name = "character_reference_contact_info")
	public String character_reference_contact_info;
	
	@Column(name = "emergency_contact_name")
	public String emergency_contact_name;
	
	@Column(name = "emergency_contact_phone_number")
	public String emergency_contact_phone_number;
	
	@Column(name = "current_home_street_address")
	public String current_home_street_address;
	
	@Column(name = "current_home_moved_in_on")
	public String current_home_moved_in_on;
	
	@Column(name = "current_home_dissatisfaction_explanation")
	public String current_home_dissatisfaction_explanation;
	
	@Column(name = "current_home_owner")
	public String current_home_owner;
	
	@Column(name = "current_home_owner_contact_info")
	public String current_home_owner_contact_info;
	
	@Column(name = "previous_home_street_address")
	public String previous_home_street_address;
	
	@Column(name = "previous_home_moved_in_on")
	public String previous_home_moved_in_on;
	
	@Column(name = "previous_home_moved_out")
	public String previous_home_moved_out;
	
	@Column(name = "previous_home_owner")
	public String previous_home_owner;
	
	@Column(name = "previous_home_owner_contact_info")
	public String previous_home_owner_contact_info;
	
	@Column(name = "employment_status")
	public String employment_status;
	
	@Column(name = "current_employment_supervisor")
	public String current_employment_supervisor;
	
	@Column(name = "cosigner_name")
	public String cosigner_name;
	
	@Column(name = "cosigner_email_address")
	public String cosigner_email_address;
	
	@Column(name = "desires_to_move_in_on")
	public String desires_to_move_in_on;

	
	public ProfileTable()
	{
		super();
	}
	
	

	public ProfileTable(String uid, String firstname, String lastname,
			String born_on, String drivers_license_number,
			String drivers_license_state, String phone_number,
			String pets_description, String vehicles_description,
			String was_ever_evicted, String was_ever_evicted_explanation,
			String is_felon, String is_felon_explanation,
			String character_reference_name,
			String character_reference_contact_info,
			String emergency_contact_name,
			String emergency_contact_phone_number,
			String current_home_street_address,
			String current_home_moved_in_on,
			String current_home_dissatisfaction_explanation,
			String current_home_owner, String current_home_owner_contact_info,
			String previous_home_street_address,
			String previous_home_moved_in_on, String previous_home_moved_out,
			String previous_home_owner,
			String previous_home_owner_contact_info, String employment_status,
			String current_employment_supervisor, String cosigner_name,
			String cosigner_email_address, String desires_to_move_in_on) {
		super();
		this.uid = uid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.born_on = born_on;
		this.drivers_license_number = drivers_license_number;
		this.drivers_license_state = drivers_license_state;
		this.phone_number = phone_number;
		this.pets_description = pets_description;
		this.vehicles_description = vehicles_description;
		this.was_ever_evicted = was_ever_evicted;
		this.was_ever_evicted_explanation = was_ever_evicted_explanation;
		this.is_felon = is_felon;
		this.is_felon_explanation = is_felon_explanation;
		this.character_reference_name = character_reference_name;
		this.character_reference_contact_info = character_reference_contact_info;
		this.emergency_contact_name = emergency_contact_name;
		this.emergency_contact_phone_number = emergency_contact_phone_number;
		this.current_home_street_address = current_home_street_address;
		this.current_home_moved_in_on = current_home_moved_in_on;
		this.current_home_dissatisfaction_explanation = current_home_dissatisfaction_explanation;
		this.current_home_owner = current_home_owner;
		this.current_home_owner_contact_info = current_home_owner_contact_info;
		this.previous_home_street_address = previous_home_street_address;
		this.previous_home_moved_in_on = previous_home_moved_in_on;
		this.previous_home_moved_out = previous_home_moved_out;
		this.previous_home_owner = previous_home_owner;
		this.previous_home_owner_contact_info = previous_home_owner_contact_info;
		this.employment_status = employment_status;
		this.current_employment_supervisor = current_employment_supervisor;
		this.cosigner_name = cosigner_name;
		this.cosigner_email_address = cosigner_email_address;
		this.desires_to_move_in_on = desires_to_move_in_on;
	}



	public String getBorn_on() {
		return born_on;
	}

	public void setBorn_on(String born_on) {
		this.born_on = born_on;
	}

	public String getDrivers_license_number() {
		return drivers_license_number;
	}

	public void setDrivers_license_number(String drivers_license_number) {
		this.drivers_license_number = drivers_license_number;
	}

	public String getDrivers_license_state() {
		return drivers_license_state;
	}

	public void setDrivers_license_state(String drivers_license_state) {
		this.drivers_license_state = drivers_license_state;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPets_description() {
		return pets_description;
	}

	public void setPets_description(String pets_description) {
		this.pets_description = pets_description;
	}

	public String getVehicles_description() {
		return vehicles_description;
	}

	public void setVehicles_description(String vehicles_description) {
		this.vehicles_description = vehicles_description;
	}

	public String getWas_ever_evicted() {
		return was_ever_evicted;
	}

	public void setWas_ever_evicted(String was_ever_evicted) {
		this.was_ever_evicted = was_ever_evicted;
	}

	public String getWas_ever_evicted_explanation() {
		return was_ever_evicted_explanation;
	}

	public void setWas_ever_evicted_explanation(String was_ever_evicted_explanation) {
		this.was_ever_evicted_explanation = was_ever_evicted_explanation;
	}

	public String getIs_felon() {
		return is_felon;
	}

	public void setIs_felon(String is_felon) {
		this.is_felon = is_felon;
	}

	public String getIs_felon_explanation() {
		return is_felon_explanation;
	}

	public void setIs_felon_explanation(String is_felon_explanation) {
		this.is_felon_explanation = is_felon_explanation;
	}

	public String getCharacter_reference_name() {
		return character_reference_name;
	}

	public void setCharacter_reference_name(String character_reference_name) {
		this.character_reference_name = character_reference_name;
	}

	public String getCharacter_reference_contact_info() {
		return character_reference_contact_info;
	}

	public void setCharacter_reference_contact_info(
			String character_reference_contact_info) {
		this.character_reference_contact_info = character_reference_contact_info;
	}

	public String getEmergency_contact_name() {
		return emergency_contact_name;
	}

	public void setEmergency_contact_name(String emergency_contact_name) {
		this.emergency_contact_name = emergency_contact_name;
	}

	public String getEmergency_contact_phone_number() {
		return emergency_contact_phone_number;
	}

	public void setEmergency_contact_phone_number(
			String emergency_contact_phone_number) {
		this.emergency_contact_phone_number = emergency_contact_phone_number;
	}

	public String getCurrent_home_street_address() {
		return current_home_street_address;
	}

	public void setCurrent_home_street_address(String current_home_street_address) {
		this.current_home_street_address = current_home_street_address;
	}

	public String getCurrent_home_moved_in_on() {
		return current_home_moved_in_on;
	}

	public void setCurrent_home_moved_in_on(String current_home_moved_in_on) {
		this.current_home_moved_in_on = current_home_moved_in_on;
	}

	public String getCurrent_home_dissatisfaction_explanation() {
		return current_home_dissatisfaction_explanation;
	}

	public void setCurrent_home_dissatisfaction_explanation(
			String current_home_dissatisfaction_explanation) {
		this.current_home_dissatisfaction_explanation = current_home_dissatisfaction_explanation;
	}

	public String getCurrent_home_owner() {
		return current_home_owner;
	}

	public void setCurrent_home_owner(String current_home_owner) {
		this.current_home_owner = current_home_owner;
	}

	public String getCurrent_home_owner_contact_info() {
		return current_home_owner_contact_info;
	}

	public void setCurrent_home_owner_contact_info(
			String current_home_owner_contact_info) {
		this.current_home_owner_contact_info = current_home_owner_contact_info;
	}

	public String getPrevious_home_street_address() {
		return previous_home_street_address;
	}

	public void setPrevious_home_street_address(String previous_home_street_address) {
		this.previous_home_street_address = previous_home_street_address;
	}

	public String getPrevious_home_moved_in_on() {
		return previous_home_moved_in_on;
	}

	public void setPrevious_home_moved_in_on(String previous_home_moved_in_on) {
		this.previous_home_moved_in_on = previous_home_moved_in_on;
	}

	public String getPrevious_home_moved_out() {
		return previous_home_moved_out;
	}

	public void setPrevious_home_moved_out(String previous_home_moved_out) {
		this.previous_home_moved_out = previous_home_moved_out;
	}

	public String getPrevious_home_owner() {
		return previous_home_owner;
	}

	public void setPrevious_home_owner(String previous_home_owner) {
		this.previous_home_owner = previous_home_owner;
	}

	public String getPrevious_home_owner_contact_info() {
		return previous_home_owner_contact_info;
	}

	public void setPrevious_home_owner_contact_info(
			String previous_home_owner_contact_info) {
		this.previous_home_owner_contact_info = previous_home_owner_contact_info;
	}

	public String getEmployment_status() {
		return employment_status;
	}

	public void setEmployment_status(String employment_status) {
		this.employment_status = employment_status;
	}

	public String getCurrent_employment_supervisor() {
		return current_employment_supervisor;
	}

	public void setCurrent_employment_supervisor(
			String current_employment_supervisor) {
		this.current_employment_supervisor = current_employment_supervisor;
	}

	public String getCosigner_name() {
		return cosigner_name;
	}

	public void setCosigner_name(String cosigner_name) {
		this.cosigner_name = cosigner_name;
	}

	public String getCosigner_email_address() {
		return cosigner_email_address;
	}

	public void setCosigner_email_address(String cosigner_email_address) {
		this.cosigner_email_address = cosigner_email_address;
	}

	public String getDesires_to_move_in_on() {
		return desires_to_move_in_on;
	}

	public void setDesires_to_move_in_on(String desires_to_move_in_on) {
		this.desires_to_move_in_on = desires_to_move_in_on;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
	
	

}
