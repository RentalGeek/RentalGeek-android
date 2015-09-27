package com.rentalgeek.android.ui.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.pojos.CosignerInviteDTO;
import com.rentalgeek.android.ui.fragment.FragmentYourCosigner;

/**
 * Created by rajohns on 9/26/15.
 *
 */
public class CosignerInviteRow extends LinearLayout {

    public CosignerInviteRow(Context context) {
        super(context);

        this.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 20);
        this.setLayoutParams(layoutParams);
        this.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void inflateView(final FragmentYourCosigner fragment, final CosignerInviteDTO invite) {
        ImageButton xButton = new ImageButton(fragment.getActivity());
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(30, 30);
        buttonParams.setMargins(15, 0, 15, 0);
        xButton.setLayoutParams(buttonParams);
        xButton.setImageResource(R.drawable.x_icon);
        xButton.setBackground(null);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.deleteInviteAlert(invite);
            }
        });

        TextView invitedNameTextView = new TextView(fragment.getActivity());
        invitedNameTextView.setTextColor(getResources().getColor(R.color.blue));
        invitedNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        invitedNameTextView.setText(rowText(invite));

        this.addView(xButton);
        this.addView(invitedNameTextView);
    }

    private String rowText(CosignerInviteDTO invite) {
        String statusText;

        if (invite.accepted != null && invite.accepted == Boolean.TRUE) {
            statusText = "(Approved)";
        } else if (invite.accepted != null && invite.accepted == Boolean.FALSE) {
            statusText = "(Rejected)";
        } else {
            statusText = "(Pending)";
        }

        return invite.invited_name + " " + statusText;
    }

}
