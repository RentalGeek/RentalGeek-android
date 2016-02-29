package com.rentalgeek.android.backend.model;


public class Lease {

    public int id;
    public int rental_offering_id;
    public int applicable_id;
    public double first_months_rent;
    public double security_deposit;
    public double total_due;
    public double total_paid;
    public String last_payment_date;
    public String applicable_type;
    public String fully_executed_lease_url;
    public String lease_document_guid;
    public String lease_document_url;
    public boolean complete;
    public boolean paid;

}
