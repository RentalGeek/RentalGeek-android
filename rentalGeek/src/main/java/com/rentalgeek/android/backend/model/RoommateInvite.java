package com.rentalgeek.android.backend.model;

public class RoommateInvite
{
    public int id;
    public int roommate_group_id;
    public int inviter_id;
    public int invited_id;

    public String inviter_name;
    public String invited_name;

    public boolean accepted;
}
