package com.rentalgeek.android.ui.activity;


import android.os.Bundle;

import com.rentalgeek.android.R;
import com.rentalgeek.android.bus.events.ClickHomeEvent;
import com.rentalgeek.android.bus.events.ShowGeekScore;
import com.rentalgeek.android.bus.events.ShowGeekScoreWait;
import com.rentalgeek.android.mvp.geekscore.GeekScorePresenter;
import com.rentalgeek.android.mvp.geekscore.GeekScoreView;
import com.rentalgeek.android.ui.Navigation;
import com.rentalgeek.android.ui.fragment.FragmentGeekScore;
import com.rentalgeek.android.ui.fragment.FragmentGeekScoreWait;

public class ActivityGeekScore extends GeekBaseActivity implements GeekScoreView {

    private GeekScorePresenter presenter;

    public ActivityGeekScore() {
        super(true, true, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_fragment);

        setupNavigation();
        setMenuItemSelected(R.id.geek_score);
        presenter = new GeekScorePresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getGeekScore();
    }

    @Override
    public void showGeekScore(String geek_score) {
        FragmentGeekScore fragment = new FragmentGeekScore();
        Bundle args = getIntent().getExtras();

        if (args == null) {
            args = new Bundle();
        }

        args.putString("GEEK_SCORE", geek_score);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void showGeekScoreWait() {
        FragmentGeekScoreWait fragment = new FragmentGeekScoreWait();
        Bundle args = getIntent().getExtras();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onEventMainThread(ClickHomeEvent event) {
        Navigation.navigateActivity(this, ActivityHome.class, true);
    }

    public void onEventMainThread(ShowGeekScore event) {
        showGeekScore(event.getScore());
    }

    public void onEventMainThread(ShowGeekScoreWait event) {
        showGeekScoreWait();
    }

}
