package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

/**
 * Created by Alan R on 10/5/15.
 */
public class FragmentNoFavorites extends GeekBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_no_favorites, viewGroup, false);
        return view;
    }
}
