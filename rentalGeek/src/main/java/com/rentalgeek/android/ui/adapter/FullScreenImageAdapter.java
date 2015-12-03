package com.rentalgeek.android.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rajohns on 10/3/15.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> photoUrls;

    public FullScreenImageAdapter(Activity activity, ArrayList<String> photoUrls) {
        this.activity = activity;
        this.photoUrls = photoUrls;
    }

    @Override
    public int getCount() {
        return photoUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_fullscreen_image, container, false);

        ImageView photoImageView = (ImageView) view.findViewById(R.id.property_photo_image_view);
        final TextView loadingTextView = (TextView) view.findViewById(R.id.loading_text_view);

        Picasso.with(activity)
                .load(photoUrls.get(position))
                .into(photoImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        loadingTextView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        loadingTextView.setText("Error loading image");
                    }
                });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
