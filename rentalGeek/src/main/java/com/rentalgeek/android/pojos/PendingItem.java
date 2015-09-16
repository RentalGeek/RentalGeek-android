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
        return Html.fromHtml("<u>" + colorOpeningTag(roommate.status) + roommate.status.toUpperCase() + colorClosingTag(roommate.status) + "</u>");
    }

    @Override
    public Spanned getRightTextForCosigner(RoommateDTO roommate) {
        return Html.fromHtml("<u>" + colorOpeningTag(roommate.cosigner_status) + roommate.cosigner_status.toUpperCase() + colorClosingTag(roommate.cosigner_status) + "</u>");
    }

    private String colorOpeningTag(String status) {
        if (status.toUpperCase().equals("PENDING") || status.toUpperCase().equals("APPLIED")) {
            return "<font color='blue'>";
        } else if(status.toUpperCase().equals("SIGNED")) {
            return "<font color='green'>";
        } else {
            return "";
        }
    }

    private String colorClosingTag(String status) {
        if (status.toUpperCase().equals("PENDING") || status.toUpperCase().equals("APPLIED") || status.toUpperCase().equals("SIGNED")) {
            return "</font>";
        } else {
            return "";
        }
    }

}
