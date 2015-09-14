package com.rentalgeek.android.ui.adapter;

import java.util.List;
import android.view.View;
import java.util.LinkedList;
import android.view.ViewGroup;
import butterknife.InjectView;
import butterknife.ButterKnife;
import android.content.Context;
import android.widget.TextView;
import com.rentalgeek.android.R;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import com.squareup.picasso.Picasso;
import com.rentalgeek.android.pojos.Rental;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.mvp.list.rental.RentalListPresenter;

public class RentalAdapter extends ArrayAdapter<Rental>{
    
    private int rowLayoutResourceId;
    private RentalListPresenter presenter;

    public RentalAdapter(Context context,int rowLayoutResourceId) {
        super(context, rowLayoutResourceId);
        this.rowLayoutResourceId = rowLayoutResourceId;
    }
    
    public void setPresenter(RentalListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        
        ViewHolder viewHolder;
     
        Rental rental = getItem(position);

        if( view == null) {
            LayoutInflater inflater = LayoutInflater.from(RentalGeekApplication.context);
            view = inflater.inflate(rowLayoutResourceId, parent, false);
       
            viewHolder = new ViewHolder(view, presenter);
            view.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //Set values
        if( rental.isStarred() ){
         Picasso
            .with(getContext())
            .load(R.drawable.star_select)
            .into(viewHolder.star_imageview);

        }

        else {
          Picasso
            .with(getContext())
            .load(R.drawable.star)
            .into(viewHolder.star_imageview);
        }

        viewHolder.star_imageview.setTag(rental.getId());
        viewHolder.price_textview.setText(String.format("%d/mo",rental.getMonthlyRent()));
        viewHolder.bathroom_count_textview.setText(String.format("%d",rental.getBathroomCount()));
        viewHolder.bedroom_count_textview.setText(String.format("%d",rental.getBedroomCount()));
        viewHolder.street_name_textview.setText(String.format("%s",rental.getHeadline()));
        
        Picasso
            .with(getContext())
            .load(rental.getImageUrl())
            .into(viewHolder.rental_imageview);
        
        return view;
    }


    static class ViewHolder implements View.OnClickListener, StarView{
        @InjectView(R.id.rental_imageview) ImageView rental_imageview;
        @InjectView(R.id.star_imageview) ImageView star_imageview;
        @InjectView(R.id.price) TextView price_textview;
        @InjectView(R.id.bathroom_count) TextView bathroom_count_textview;
        @InjectView(R.id.street_name) TextView street_name_textview;
        @InjectView(R.id.bedroom_count) TextView bedroom_count_textview;
        
        private RentalListPresenter presenter;

        public ViewHolder(View view, RentalListPresenter presenter) {
                ButterKnife.inject(this,view);
                this.presenter = presenter;
                star_imageview.setOnClickListener(this);
        }

        @Override
        public void selectStar() {
            if( rental_imageview != null) {
                 Picasso
                    .with(rental_imageview.getContext())
                    .load(R.drawable.star_select)
                    .into(star_imageview);
            }
        }

        @Override
        public void unselectStar() {
            if( rental_imageview != null ) {
                 Picasso
                    .with(rental_imageview.getContext())
                    .load(R.drawable.star)
                    .into(star_imageview);
            }
        }
        
        @Override
        public void onClick(View view) {
                String rental_id = (String) view.getTag();
                presenter.select(rental_id, this);
        }
    }
}
