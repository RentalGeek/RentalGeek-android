package com.rentalgeek.android.backend;

import java.util.List;

public class ErrorObj {

	public Error errors;

	public class Error {
		public List<String> phone_number;
		public List<String> born_on;
		public List<String> emergency_contact_phone_number;
		public List<String> ssn;
		public List<String> cosigner_email_address;
		public List<String> previous_employment_employer_phone_number;
		public List<String> previous_employment_employer_email_address;
		public List<String> current_employment_employer_email_address;
		public List<String> current_employment_employer_phone_number;
		public List<String> previous_home_moved_in_on;
		public List<String> current_home_moved_in_on;
		public List<String> desires_to_move_in_on;
		public List<String> email;
	}

}
