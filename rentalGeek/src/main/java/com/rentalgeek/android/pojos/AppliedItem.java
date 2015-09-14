package com.rentalgeek.android.pojos;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by rajohns on 9/14/15.
 *
 */
public class AppliedItem extends ApplicationItem {

    public AppliedItem(ApplicationDTO applicationDTO) {
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

        return Html.fromHtml("<u>" + roommate.cosigner_status.toUpperCase() + "</u>");
    }

}
