package com.rentalgeek.android.utils;

/**
 * Created by rajohns on 9/17/15.
 *
 */
public enum CosignerDestinationPage {

    INSTANCE;

    private String destination = "";
    private String nameOfInviter = "";
    private int inviteId;

    public static CosignerDestinationPage getInstance() {
        return INSTANCE;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getNameOfInviter() {
        return nameOfInviter;
    }

    public void setNameOfInviter(String nameOfInviter) {
        this.nameOfInviter = nameOfInviter;
    }

    public int getInviteId() {
        return inviteId;
    }

    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }

}
