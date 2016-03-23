package com.rentalgeek.android.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.RentalGeekApplication;
import com.rentalgeek.android.bus.AppEventBus;
import com.rentalgeek.android.bus.events.ClickRentalEvent;
import com.rentalgeek.android.bus.events.ClickStarEvent;
import com.rentalgeek.android.bus.events.RemoveItemEvent;
import com.rentalgeek.android.constants.IntentKey;
import com.rentalgeek.android.mvp.common.StarView;
import com.rentalgeek.android.pojos.Rental;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RentalAdapter extends ArrayAdapter<Rental> {

    private int rowLayoutResourceId;

    public RentalAdapter(Context context, int rowLayoutResourceId) {
        super(context, rowLayoutResourceId);
        this.rowLayoutResourceId = rowLayoutResourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder viewHolder;
        Rental rental = getItem(position);

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(RentalGeekApplication.context);
            view = inflater.inflate(rowLayoutResourceId, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (rental.isStarred()) {
            Picasso.with(getContext()).load(R.drawable.star_full).into(viewHolder.star_imageview);

        } else {
            Picasso.with(getContext()).load(R.drawable.star_outline).into(viewHolder.star_imageview);
        }

        viewHolder.position = position;
        viewHolder.star_imageview.setTag(rental.getId());
        viewHolder.price_textview.setText(String.format("$%d", rental.getMonthlyRent()));

        viewHolder.room_count_textview.setText(String.format("%d BR, %d Bath", rental.getBedroomCount(), rental.getBathroomCount()));

        String format = "%s\n%s, %s %s";
        viewHolder.address_textview.setText(String.format(format, rental.getAddress(), rental.getCity(), rental.getState(), rental.getZipcode()));

        Picasso.with(getContext()).load(rental.getImageUrl()).into(viewHolder.rental_imageview);

        return view;
    }


    public static class ViewHolder implements StarView {
        @InjectView(R.id.rental_imageview) ImageView rental_imageview;
        @InjectView(R.id.star_imageview) ImageView star_imageview;
        @InjectView(R.id.price) TextView price_textview;
        @InjectView(R.id.room_count) TextView room_count_textview;
        @InjectView(R.id.address) TextView address_textview;
        int position;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @Override
        public void selectStar() {
            if (rental_imageview != null) {
                Picasso.with(rental_imageview.getContext()).load(R.drawable.star_full).into(star_imageview);
            }
        }

        @Override
        public void unselectStar() {
            if (rental_imageview != null) {
                Picasso.with(rental_imageview.getContext()).load(R.drawable.star_outline).into(star_imageview);
            }

            AppEventBus.post(new RemoveItemEvent(position));
        }

        @OnClick(R.id.rental_imageview)
        public void OnRentalClick() {
            String rental_id = (String) star_imageview.getTag();
            Bundle bundle = new Bundle();
            bundle.putString(IntentKey.RENTAL_ID, rental_id);
            AppEventBus.post(new ClickRentalEvent(bundle));
        }

        @OnClick(R.id.star_imageview)
        public void onStarClick() {
            String rental_id = (String) star_imageview.getTag();
            Bundle bundle = new Bundle();
            bundle = new Bundle();
            bundle.putString(IntentKey.RENTAL_ID, rental_id);
            bundle.putInt("POSITION", position);
            AppEventBus.post(new ClickStarEvent(bundle));
        }
    }

}
