package com.rentalgeek.android.utils;

import android.app.Activity;
import android.content.Intent;

import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.CosignerProfile;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp1;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp2;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp3;
import com.rentalgeek.android.ui.activity.ActivityCosignerApp4;
import com.rentalgeek.android.ui.activity.ActivityCosignerInvite;
import com.rentalgeek.android.ui.activity.ActivityCosignerList;
import com.rentalgeek.android.ui.preference.AppPreferences;

import java.util.ArrayList;

public enum CosignerDestinationLogic {

    INSTANCE;

    private String nameOfInviter = "";
    private Integer inviteId;
    private ArrayList<CosignerInviteDTO> cosignerInvites;

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

    public ArrayList<CosignerInviteDTO> getCosignerInvites() {
        return cosignerInvites;
    }

    public void setCosignerInvites(ArrayList<CosignerInviteDTO> cosignerInvites) {
        this.cosignerInvites = cosignerInvites;

        if (!hasOutstandingInvites() && !hasAcceptedAtLeastOneCosignerInvite()) {
            SessionManager.Instance.getCurrentUser().is_cosigner = false;
        }
    }

    public void updateInvite(CosignerInviteDTO updatedInvite) {
        for (CosignerInviteDTO cosignerInviteDTO : cosignerInvites) {
            if (cosignerInviteDTO.id.intValue() == updatedInvite.id.intValue()) {
                cosignerInviteDTO.accepted = updatedInvite.accepted;
            }
        }

        setCosignerInvites(cosignerInvites);
    }

    public boolean hasOutstandingInvites() {
        for (CosignerInviteDTO cosignerInviteDTO : cosignerInvites) {
            if (cosignerInviteDTO.accepted == null) {
                setNameOfInviter(cosignerInviteDTO.inviter_name);
                setInviteId(cosignerInviteDTO.id);
                return true;
            }
        }

        return false;
    }

    public boolean hasAcceptedAtLeastOneCosignerInvite() {
        for (CosignerInviteDTO cosignerInviteDTO : cosignerInvites) {
            if (Boolean.TRUE.equals(cosignerInviteDTO.accepted)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasCompletedCosignerProfile() {
        CosignerProfile cosignerProfile = SessionManager.Instance.getCosignerProfile();

        if (cosignerProfile != null) {
            return Boolean.TRUE.equals(cosignerProfile.completed);
        } else {
            return false;
        }
    }

    public void navigateToNextCosignActivity(Activity activity) {
        if (!SessionManager.Instance.getCurrentUser().is_cosigner) {
            activity.finish();
            return;
        }

        if (hasOutstandingInvites()) {
            activity.startActivity(new Intent(activity, ActivityCosignerInvite.class));
        } else if (!hasCompletedCosignerProfile()) {
            goToCurrentPageInCosignerApplication(activity);
        } else {
            activity.startActivity(new Intent(activity, ActivityCosignerList.class));
        }
    }

    private void goToCurrentPageInCosignerApplication(Activity activity) {
        int currentCosignerAppPage = AppPreferences.getCosignerProfilePosition();
        switch (currentCosignerAppPage) {
            case 1:
                activity.startActivity(new Intent(activity, ActivityCosignerApp1.class));
                break;
            case 2:
                activity.startActivity(new Intent(activity, ActivityCosignerApp2.class));
                break;
            case 3:
                activity.startActivity(new Intent(activity, ActivityCosignerApp3.class));
                break;
            case 4:
                activity.startActivity(new Intent(activity, ActivityCosignerApp4.class));
                break;
            default:
                activity.startActivity(new Intent(activity, ActivityCosignerApp1.class));
                break;
        }
    }

}
