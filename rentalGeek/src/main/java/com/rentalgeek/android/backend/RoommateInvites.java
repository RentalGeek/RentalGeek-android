package com.rentalgeek.android.backend;

import java.util.List;

public class RoommateInvites {

    public int id;
    public String name;

	public List<RoommateInvite> roommate_invites;
	
	public class RoommateInvite
	{
		public String id;
        public String roommate_group_id;
        public String inviter_id;
        public String inviter_name;
        public String invited_name;
	}

}
