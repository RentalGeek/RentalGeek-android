package com.rentalgeek.android.backend;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

        @SerializedName("user_id")
        @Expose
        private List<String> userId = new ArrayList<String>();

        /**
         * @return The userId
         */
        public List<String> getApplicantId() {
            return userId;
        }

        /**
         * @param userId The user_id
         */
        public void setApplicantId(List<String> applicantId) {
            this.userId = userId;
        }

    }

    @Expose
    private Errors errors;

    /**
     * @return The error
     */
    public Errors getErrors() {
        return errors;
    }

    /**
     * @param errors The error
     */
    public void setErrors(Errors errors) {
        this.errors = errors;
    }

}
