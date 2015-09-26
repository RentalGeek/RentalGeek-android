package com.rentalgeek.android.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.api.SessionManager;
import com.rentalgeek.android.backend.model.Profile;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rajohns on 9/20/15.
 *
 */
public class FragmentYourCosigner extends GeekBaseFragment {

    private Profile profile;

    @InjectView(R.id.has_cosigner_layout) LinearLayout hasCosignerLayout;
    @InjectView(R.id.no_cosigner_layout) LinearLayout noCosignerLayout;
    @InjectView(R.id.cosigner_name_textview) TextView cosignerNameTextView;
    @InjectView(R.id.add_button) Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_cosigner, container, false);
        ButterKnife.inject(this, view);

        profile = SessionManager.Instance.getDefaultProfile();

        if (profile != null && (profile.cosigner_email_address != null || profile.cosigner_name != null)) {
            hasCosignerLayout.setVisibility(View.VISIBLE);
            noCosignerLayout.setVisibility(View.GONE);

            showCosignerName();
            disableAddButton();
        }

        return view;
    }

    private void showCosignerName() {
        if (profile.cosigner_name != null) {
            cosignerNameTextView.setText(profile.cosigner_name);
            return;
        }

        cosignerNameTextView.setText(profile.cosigner_email_address);
    }

    private void disableAddButton() {
        addButton.setEnabled(false);
        addButton.setBackgroundColor(getActivity().getResources().getColor(R.color.decline_gray));
        addButton.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    @OnClick(R.id.add_button)
    public void addButtonTapped() {
        Log.d("tag", "add tapped");
    }

    @OnClick(R.id.x_button)
    public void xButtonTapped() {
        Log.d("tag", "x tapped");
        String cosignerString = profile.cosigner_name != null ? profile.cosigner_name : profile.cosigner_email_address;

        new AlertDialog.Builder(getActivity())
            .setTitle("Remove Cosigner")
            .setMessage("Are you sure you want to remove " + cosignerString + " as your cosigner?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    removeCosigner();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
            .show();
    }

    private void removeCosigner() {

    }

}
