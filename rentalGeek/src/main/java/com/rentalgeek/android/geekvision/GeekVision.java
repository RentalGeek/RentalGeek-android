package com.rentalgeek.android.geekvision;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author George
 * @purpose This class which shows the Augment reality[ Current work in progress]
 */
public class GeekVision extends Fragment {

    private YoYo.YoYoString animation_obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.geekvision, container, false);
        ButterKnife.inject(this, v);
        return v;
    }


    @OnClick(R.id.coming_soon)
    public void ComingSoon(View v) {
        animation_obj = YoYo.with(Techniques.Tada).duration(1000).playOn(v);
    }

}
