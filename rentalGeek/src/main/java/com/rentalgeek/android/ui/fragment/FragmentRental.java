package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.ShowProfileCreationEvent;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.mvp.rental.RentalPresenter;
import com.rentalgeek.android.mvp.rental.RentalView;
import com.rentalgeek.android.pojos.Rental;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FragmentRental extends GeekBaseFragment implements RentalView, StarView {
    
    @InjectView(R.id.price) TextView price_textview;
    @InjectView(R.id.rental_image) ImageView rental_imageview;
    @InjectView(R.id.room_count) TextView room_count_textview;
    @InjectView(R.id.address) TextView address_textview;
    @InjectView(R.id.star_image) ImageView star_imageview;
    @InjectView(R.id.description) TextView description_textview;
    @InjectView(R.id.amenities) TextView amenities_textview;
    @InjectView(R.id.apply_btn) Button apply_btn;

    private RentalPresenter presenter;
    private boolean fullView = false;
    private String rental_id;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        presenter = new RentalPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_rental,container,false);
        ButterKnife.inject(this,view);
        
        if( ! fullView ) {
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
        }

        return view;
    }

    @Override 
    public void goToCreateProfile() {
        AppEventBus.post(new ShowProfileCreationEvent());
    }
    
    public void setFullView(boolean fullView) {
        this.fullView = fullView;
    }
    
    @Override
    public void onStart() {
        super.onStart();       
            if( getArguments() != null ) {
                Bundle args = getArguments();
                rental_id = args.getString("RENTAL_ID");
            }
            
            presenter.getRental(rental_id);
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

        if( rental == null )
            return;
        
        rental_id = rental.getId();
        
        star_imageview.setTag(rental.getId());
        price_textview.setText(String.format("$%d",rental.getMonthlyRent()));

        room_count_textview.setText(String.format("%d BR, %d Bath",rental.getBedroomCount(),rental.getBathroomCount()));

        address_textview.setText(String.format("%s\n%s, %s %s",rental.getAddress(),rental.getCity(),rental.getState(),rental.getZipcode()));
 
        description_textview.setText(rental.getDescription());
        
        if( rental.applied() ) {
            String applied_text = RentalGeekApplication.getResourceString(R.string.applied_text);
            apply_btn.setText(applied_text);
            apply_btn.setClickable(false);
        }

        StringBuilder amenities = new StringBuilder();
        
        for(String amenity : rental.getAmenities() ) {
            amenities.append(String.format("\u2022 %s\n",amenity));
        }

        amenities_textview.setText(amenities);

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

        show();
    }

    @OnClick(R.id.rental_image) void onRentalClick() {
        if( ! fullView ) {

            Bundle bundle = new Bundle();
            bundle.putString("RENTAL_ID",rental_id);

            AppEventBus.post(new ClickRentalEvent(bundle));
        }
    }

    @Override
    public void selectStar() {
        if( rental_imageview != null) {
             Picasso
                .with(getActivity())
                .load(R.drawable.star_full)
                .into(star_imageview);
        }
    }

    @Override
    public void unselectStar() {
        if( rental_imageview != null ) {
              Picasso
                .with(getActivity())
                .load(R.drawable.star_outline)
                .into(star_imageview);
        }
    }
    
    @OnClick(R.id.star_image)
    public void star(View view) {
        presenter.select(rental_id,this);
    }

    @OnClick(R.id.apply_btn)
    public void onApplyClick() {
        presenter.apply(rental_id);
    }
}
