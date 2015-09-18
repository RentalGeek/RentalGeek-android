package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.utils.CosignerDestinationPage;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentCosignerInvite extends GeekBaseFragment {

    @InjectView(R.id.invite_cosign_textview) TextView inviteTextView;
    @InjectView(R.id.accept_button) Button acceptButton;
    @InjectView(R.id.decline_button) Button declineButton;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_invite, container, false);
		ButterKnife.inject(this,view);
        
        String inviteText = getResources().getString(R.string.invite_cosign_text);
        inviteText = String.format(inviteText, CosignerDestinationPage.getInstance().getNameOfInviter());
        this.inviteTextView.setText(inviteText);

		return view;
	}

    @OnClick(R.id.accept_button)
    public void acceptInvite() {
        Log.d("tag", "accept invite");
        // make call to POST /api/v1/cosigner_invites/:id/accept
    }

    @OnClick(R.id.decline_button)
    public void declineInvite() {
        Log.d("tag", "decline invite");
        // make call to POST /api/v1/cosigner_invites/:id/deny
    }

}
