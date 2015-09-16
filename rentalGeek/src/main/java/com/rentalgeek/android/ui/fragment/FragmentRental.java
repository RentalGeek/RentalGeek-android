package com.rentalgeek.android.ui.fragment;

import android.view.View;
import android.os.Bundle;
import butterknife.OnClick;
import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.ButterKnife;
import android.widget.TextView;
import android.widget.ImageView;
import com.rentalgeek.android.R;
import android.view.LayoutInflater;
import com.squareup.picasso.Picasso;
import android.view.ViewTreeObserver;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.RentalGeekApplication;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.rentalgeek.android.mvp.rental.RentalPresenter;

public class FragmentRental extends GeekBaseFragment implements RentalView, StarView {
    
    @InjectView(R.id.price) TextView price_textview;
    @InjectView(R.id.rental_image) ImageView rental_imageview;
    @InjectView(R.id.bathroom_count) TextView bathroom_count_textview;
    @InjectView(R.id.bedroom_count) TextView bedroom_count_textview;
    @InjectView(R.id.street_name) TextView street_name_textview;
    @InjectView(R.id.star_image) ImageView star_imageview;
    
    private RentalPresenter presenter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new RentalPresenter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_rental,container,false);
        ButterKnife.inject(this,view);
        
        hide();

        //PreDraw guarantees measuring of screen size.
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                
                getView().getViewTreeObserver().removeOnPreDrawListener(this);
                
        
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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

        if( rental == null)
            return;
        
        String rental_id = rental.getId();

        price_textview.setText(String.format("%d/mo",rental.getMonthlyRent()));
        bathroom_count_textview.setText(String.format("%d",rental.getBathroomCount()));
        bedroom_count_textview.setText(String.format("%d",rental.getBedroomCount()));
        street_name_textview.setText(rental.getHeadline());
        
        Picasso
            .with(getActivity())
            .load(rental.getImageUrl())
            .into(rental_imageview);

        if( rental.isStarred() ) {
            selectStar();
        }

        else {
            unselectStar();
        }

        star_imageview.setTag(rental_id);

        show();
    }

    @Override
    public void selectStar() {
        if( rental_imageview != null) {
             Picasso
                .with(getActivity())
                .load(R.drawable.star_select)
                .into(star_imageview);
        }
    }

    @Override
    public void unselectStar() {
        if( rental_imageview != null ) {
              Picasso
                .with(getActivity())
                .load(R.drawable.star)
                .into(star_imageview);
        }
    }
    
    @OnClick(R.id.star_image)
    public void star(View view) {
        String rental_id = (String) star_imageview.getTag();
        presenter.select(rental_id,this);
    }
}
