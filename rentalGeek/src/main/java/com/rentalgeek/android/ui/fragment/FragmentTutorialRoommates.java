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

public class FragmentTutorialRoommates extends Fragment {

    @InjectView(R.id.background_image_view) ImageView backgroundImageView;
    @InjectView(R.id.title_text_view) TextView titleTextView;
    @InjectView(R.id.first_text_view) TextView firstTextView;
    @InjectView(R.id.second_text_view) TextView secondTextView;

    public static FragmentTutorialRoommates newInstance() {
        return new FragmentTutorialRoommates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_base, container, false);
        ButterKnife.inject(this, v);

        backgroundImageView.setImageResource(R.drawable.tutorial_roommates_bg);
        titleTextView.setText("Invite\nRoommates");
        firstTextView.setText("Cool, right?");
        secondTextView.setText("Invite your roommates to\napply too! Do it all from right\ninside the app so it's not\nsuch a pain. Easy, digital,\nand fast, right?");

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
