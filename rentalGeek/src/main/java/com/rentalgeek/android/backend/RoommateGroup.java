package com.rentalgeek.android.backend;

import java.util.List;

public class RoommateGroup {

    public int id;
    public String name;

	public List<RoommateInvites> roommate_invites;
	
	public class RoommateInvites
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
