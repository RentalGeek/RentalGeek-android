package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by rajohns on 9/12/15.
 *
 */
public class FragmentPropertiesApproved extends GeekBaseFragment {

    @InjectView(R.id.recyclerView) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cosigner_list, container, false);
        ButterKnife.inject(this, view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}
