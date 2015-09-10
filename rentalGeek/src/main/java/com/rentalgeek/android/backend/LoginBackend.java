package com.rentalgeek.android.backend;

import android.text.TextUtils;

public class LoginBackend {

	public User user;

    public RegistrationError errors;

	public class User {

		public String id;
		public String full_name;
		public String first_name;
		public String last_name;
		public String email;
		public String authentication_token;
		public String avatar;
		public String google;
		public String facebook;
		public String linkedin;
		public boolean payment;
		public String has_rental_complex;
		public String profile_id;
        public String cosigner_profile_id;
		public String property_manager_id;
		public String co_signer_id;
		public String roommate_group_id;
		public String completed_lease_id;
        public boolean is_cosigner;
        public boolean allow_push_notifications;

        public boolean hasProfileId() {
            return !TextUtils.isEmpty(profile_id);
        }

	}

}
