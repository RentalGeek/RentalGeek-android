package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by rajohns on 9/14/15.
 *
 */
public class PendingItem extends ApplicationItem {

    public PendingItem(ApplicationDTO applicationDTO) {
        super(applicationDTO);
    }

    @Override
    public Spanned getLeftTextForRoomate(RoommateDTO roommate) {
        String text = "";

        text += "Approval from   <b>" + getNameText(roommate.full_name) + "</b>";
        text = text.replace("  ", "&nbsp;&nbsp;");
        return Html.fromHtml(text);
    }

    @Override
    public Spanned getLeftTextForCosigner(RoommateDTO roommate) {
        String text = "";

        text += "Approval from   <b>" + getNameText(roommate.cosigner_full_name) + "</b>";
        text = text.replace("  ", "&nbsp;&nbsp;");
        return Html.fromHtml(text);
    }

    @Override
    public Spanned getRightTextForRoommate(RoommateDTO roommate) {
        if (roommate.status == null) {
            return Html.fromHtml("APPLY");
        }

        return Html.fromHtml("<u>" + roommate.status.toUpperCase() + "</u>");
    }

    @Override
    public Spanned getRightTextForCosigner(RoommateDTO roommate) {
        if (roommate.cosigner_status == null) {
            return Html.fromHtml("APPLY");
        }

        return Html.fromHtml("<u>" + colorOpeningTag(roommate.cosigner_status) + roommate.cosigner_status.toUpperCase() + colorClosingTag(roommate.cosigner_status) + "</u>");
    }

    private String colorOpeningTag(String status) {
        if (status.toUpperCase().equals("PENDING")) {
            return "<font color='blue'>";
        } else {
            return "";
        }
    }

    private String colorClosingTag(String status) {
        if (status.toUpperCase().equals("PENDING")) {
            return "</font>";
        } else {
            return "";
        }
    }

}
