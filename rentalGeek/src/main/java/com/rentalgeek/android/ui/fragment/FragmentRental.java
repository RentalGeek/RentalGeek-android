package com.rentalgeek.android.ui.fragment;

import android.view.View;
import android.os.Bundle;
import butterknife.OnClick;
import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.ButterKnife;
import android.widget.TextView;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.RentalGeekApplication;
import android.view.ViewTreeObserver.OnPreDrawListener;

public class FragmentRental extends GeekBaseFragment implements RentalView {
    
    @InjectView(R.id.price) TextView price_textview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental,container,false);
        ButterKnife.inject(this,view);
        
        //PreDraw guarantees measuring of screen size.
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getView().getViewTreeObserver().removeOnPreDrawListener(this);
                
                getView().setVisibility(View.GONE);//Hack solution since XML attribute visibility isnt respected on a fragment
                int screenHeight = RentalGeekApplication.getScreenHeight();
                ViewGroup.LayoutParams params = getView().getLayoutParams();
                params.height = screenHeight/2;

                getView().setLayoutParams(params);
                
                return true;
            }
        });

        return view;
    }

    @Override
    public void show() {
        getFragmentManager().
                 beginTransaction().
        setCustomAnimations( R.anim.abc_slide_in_bottom,0).
                         show(this).
                           commit();
    }

    @Override
    public void hide() {
        getFragmentManager().
                 beginTransaction().
        setCustomAnimations(0,R.anim.abc_slide_out_bottom).
                         hide(this).
                           commit();
    }

    @Override
    public void showRental(Rental rental) {
        price_textview.setText(Integer.toString(rental.getMonthlyRent())+"/mo");
        show();
    }
}
