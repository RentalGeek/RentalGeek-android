package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentTutorialCosigner extends Fragment {

    @InjectView(R.id.background_image_view) ImageView backgroundImageView;
    @InjectView(R.id.title_text_view) TextView titleTextView;
    @InjectView(R.id.first_text_view) TextView firstTextView;
    @InjectView(R.id.second_text_view) TextView secondTextView;

    public static FragmentTutorialCosigner newInstance() {
        return new FragmentTutorialCosigner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_base, container, false);
        ButterKnife.inject(this, v);

        backgroundImageView.setImageResource(R.drawable.tutorial_cosigner_bg);
        titleTextView.setText("Invite\nCosigner");
        firstTextView.setText("We thought of that too!");
        secondTextView.setText("Send a request to your co-\nsigner right from the app.\nThey can use the app to fill\nout their application and\ncheck on the status.");

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
