package com.rentalgeek.android.pojos;

import java.util.ArrayList;

/**
 * Created by rajohns on 9/10/15.
 *
 */
public class Application {

    public Integer id;
    public Integer user_id;
    public Integer rental_offering_id;
    public Boolean accepted;
    public Boolean as_cosigner;
    public String access_token;
    public String signed_lease_url;
    public Integer lease_id;
    public String unsigned_lease_document_url;
    public ArrayList<Roommate> roommates;
    public RentalOffering rental_offering;

}
