package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentTutorialGeekScore extends Fragment {

    @InjectView(R.id.background_layout)
    LinearLayout backgroundLayout;
    @InjectView(R.id.title_text_view)
    TextView titleTextView;
    @InjectView(R.id.first_text_view)
    TextView firstTextView;
    @InjectView(R.id.second_text_view)
    TextView secondTextView;

    public static FragmentTutorialGeekScore newInstance() {
        return new FragmentTutorialGeekScore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tutorial_base, container, false);
        ButterKnife.inject(this, v);

        backgroundLayout.setBackgroundResource(R.drawable.tutorial_geekscore_bg);
        titleTextView.setText(getActivity().getString(R.string.geek_score_tm));
        firstTextView.setText("Get it done for $25!\nYour GeekScore is all you\nneed to apply to your first,\nsecond and even third\nchoices.");
        secondTextView.setText("It's your info so keep it\nsecure in one app!");

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
