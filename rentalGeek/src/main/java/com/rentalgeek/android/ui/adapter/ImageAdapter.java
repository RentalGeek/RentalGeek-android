package com.rentalgeek.android.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ImageAdapter extends PagerAdapter {

    Context context;

    CopyOnWriteArrayList<HashMap> photos = new CopyOnWriteArrayList<HashMap>();

//    private int[] GalImages = new int[] {
//            R.drawable.one,
//            R.drawable.two,
//            R.drawable.three
//    };
    ImageAdapter(Context context, List<HashMap> photos){
        this.context=context;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
//        int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
//        imageView.setPadding(padding, padding, padding, padding);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        imageView.setImageResource(photos.get(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}