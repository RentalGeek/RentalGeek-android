package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;
import com.rentalgeek.android.ui.activity.ActivityPropertyPhoto;
import com.rentalgeek.android.ui.adapter.FullScreenImageAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragmentPropertyPhoto extends GeekBaseFragment {

    @InjectView(R.id.photo_pager) ViewPager photoPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_photo, container, false);
        ButterKnife.inject(this, view);

        ArrayList<String> photoUrls = getArguments().getStringArrayList(ActivityPropertyPhoto.PHOTO_URLS);
        int originalPosition = getArguments().getInt(ActivityPropertyPhoto.ORIGINAL_POSITION);

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(getActivity(), photoUrls);
        photoPager.setAdapter(adapter);
        photoPager.setCurrentItem(originalPosition);

        return view;
    }

}
