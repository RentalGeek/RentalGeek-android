package com.rentalgeek.android.backend.model;


import java.util.List;

public class RoommateGroup {

    public int id;
    public int owner_id;
    public String name;

    public int[] user_ids;
    public List<RoommateInvite> roommate_invites;

}