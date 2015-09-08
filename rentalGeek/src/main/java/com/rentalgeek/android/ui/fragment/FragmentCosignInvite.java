package com.rentalgeek.android.ui.fragment;

import android.view.View;
import android.os.Bundle;
import butterknife.InjectView;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;

public class FragmentCosignInvite extends Fragment {

	private static final String TAG = "FragmentCosignInvite";
    @InjectView(R.id.invite_cosign_textview) TextView inviteTextView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cosign_invite,container,false);

		ButterKnife.inject(this,view);
        
        String inviteText = getResources().getString(R.string.invite_cosign_text);
        inviteText = String.format(inviteText,"Alan Ruvalcaba");
        this.inviteTextView.setText(inviteText);

		return view;
	}
}
