package com.rentalgeek.android.pojos;

import com.rentalgeek.android.api.SessionManager;

/**
 * Created by rajohns on 9/14/15.
 */
public class CosignItem extends ApplicationItem {

    public CosignItem() {
    }

    public CosignItem(ApplicationDTO applicationDTO) {
        super(applicationDTO);
        this.roommates = applicationDTO.cosigner_roommates;
    }

    @Override
    public String getButtonText() {
        if (Boolean.TRUE.equals(getAccepted())) {
            if (getSignedLeaseOn() != null) {
                return VIEW_LEASE;
            } else {
                return SIGN_LEASE;
            }
        } else {
            if (getUserId() != null && getUserId().toString().equals(SessionManager.Instance.getCurrentUser().id)) {
                return APPROVED;
            } else {
                return APPROVE;
            }
        }
    }

}
