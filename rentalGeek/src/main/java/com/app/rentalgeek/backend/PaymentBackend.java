package com.app.rentalgeek.backend;

public class PaymentBackend {
	
	public Transaction transaction;
	
	
	public class Transaction
	{
		   public String id;
	       public String transaction_id;
	       public String applicant_id;
	       public String created_at;
	       public String amount;
	       public String card_type;
	       public String cardholder_name;
	       public String purchased_type;
	}

}
