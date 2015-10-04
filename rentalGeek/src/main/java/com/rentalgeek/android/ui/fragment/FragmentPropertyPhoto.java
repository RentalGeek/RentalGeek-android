package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.activity.ActivityPropertyPhoto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 10/3/15.
 *
 */
public class FragmentPropertyPhoto extends GeekBaseFragment {

    @InjectView(R.id.property_photo_image_view) ImageView propertyPhotoImageView;
    @InjectView(R.id.loading_text_view) TextView loadingTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_photo, container, false);
        ButterKnife.inject(this, view);

        String photoUrl = getArguments().getString(ActivityPropertyPhoto.PHOTO_URL);

        Picasso.with(getActivity())
            .load(photoUrl)
            .into(propertyPhotoImageView, new Callback() {
                @Override
                public void onSuccess() {
                    loadingTextView.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    loadingTextView.setText("Error loading image");
                }
            });

        return view;
    }

}
