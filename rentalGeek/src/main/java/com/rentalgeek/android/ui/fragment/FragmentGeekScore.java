package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ClickHomeEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Alan R on 10/3/15.
 */
public class FragmentGeekScore extends GeekBaseFragment {

    private static final String TAG = "FragmentGeekScore";

    @InjectView(R.id.geek_score)
    TextView geek_score_textview;

    @InjectView(R.id.home_button)
    Button home_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_geekscore, viewGroup, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            String geek_score = bundle.getString("GEEK_SCORE");

            if (geek_score != null && !geek_score.isEmpty())
                geek_score_textview.setText(geek_score);
        }

        return view;
    }

    @OnClick(R.id.home_button)
    public void onHomeClick() {
        AppEventBus.post(new ClickHomeEvent());
    }
}
