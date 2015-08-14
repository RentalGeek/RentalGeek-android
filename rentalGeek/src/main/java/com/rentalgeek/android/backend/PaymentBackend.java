package com.rentalgeek.android.backend;

public class PaymentBackend {
	
	public Transaction transaction;
	
	
	public class Transaction
	{
		   public String id;
	       public String transaction_id;
	       public String user_id;
	       public String created_at;
	       public String amount;
	       public String card_type;
	       public String cardholder_name;
	       public String purchased_type;
	}

}
