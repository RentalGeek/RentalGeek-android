package com.rentalgeek.android.backend;

import java.util.List;

public class RegistrationBackend {
	
	public Applicant applicant;
	public List error;
	
	public class Applicant
	{
		public int id;
		public String email;
		public	String authentication_token;
		
	}

}
