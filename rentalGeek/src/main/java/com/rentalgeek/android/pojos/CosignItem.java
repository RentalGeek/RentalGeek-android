package com.rentalgeek.android.pojos;

/**
 * Created by rajohns on 9/14/15.
 *
 */
public class CosignItem extends ApplicationItem {

    public CosignItem() { }

    public CosignItem(ApplicationDTO applicationDTO) {
        super(applicationDTO);
        this.roommates = applicationDTO.cosigner_roommates;
    }

    @Override
    public String getButtonText() {
        if (getAccepted() != null && getAccepted() == Boolean.TRUE) {
            return SIGN_LEASE;
        }

        return APPROVE;
    }
}
