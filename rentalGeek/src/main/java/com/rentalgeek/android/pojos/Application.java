package com.rentalgeek.android.pojos;

import java.util.ArrayList;

/**
 * Created by rajohns on 9/10/15.
 *
 */
public class Application {

    private Integer id;
    private Integer user_id;
    private Integer rental_offering_id;
    private Boolean accepted;
    private Boolean as_cosigner;
    private String access_token;
    private String signed_lease_url;
    private Integer lease_id;
    private String unsigned_lease_document_url;
    private ArrayList<Roommate> roommates;
    private RentalOffering rental_offering;

}
