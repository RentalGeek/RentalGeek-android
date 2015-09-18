package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentCosignerInvite extends GeekBaseFragment {

    @InjectView(R.id.invite_cosign_textview) TextView inviteTextView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosigner_invite, container, false);
		ButterKnife.inject(this,view);
        
        String inviteText = getResources().getString(R.string.invite_cosign_text);
        inviteText = String.format(inviteText,"Alan Ruvalcaba");
        this.inviteTextView.setText(inviteText);

		return view;
	}
}
