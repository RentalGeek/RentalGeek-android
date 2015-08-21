package com.rentalgeek.android.backend;

public class RegistrationBackend {
	
	public Applicant user;
	public RegistrationError errors;
	
	public class Applicant
	{
		public int id;
		public String email;
		public	String authentication_token;
		
	}

}
