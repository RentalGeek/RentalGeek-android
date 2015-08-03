package com.app.rentalgeek.backend;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddStarBack {
	
	public StarredProperty starred_property;
	public String error;

	public class StarredProperty {

		public String id;
		public String applicant_id;
		public String rental_offering_id;
		public String property_address;
		public String bedroom_count;
		public String full_bathroom_count;
		public String square_footage_floor;
		public String monthly_rent_floor;
		public String salesy_description;
		public String image;
		public String sold_out;

	}

	public class Errors {

		@SerializedName("applicant_id")
		@Expose
		private List<String> applicantId = new ArrayList<String>();

		/**
		 * 
		 * @return The applicantId
		 */
		public List<String> getApplicantId() {
			return applicantId;
		}

		/**
		 * 
		 * @param applicantId
		 *            The applicant_id
		 */
		public void setApplicantId(List<String> applicantId) {
			this.applicantId = applicantId;
		}

	}

	@Expose
	private Errors errors;

	/**
	 * 
	 * @return The errors
	 */
	public Errors getErrors() {
		return errors;
	}

	/**
	 * 
	 * @param errors
	 *            The errors
	 */
	public void setErrors(Errors errors) {
		this.errors = errors;
	}

}
