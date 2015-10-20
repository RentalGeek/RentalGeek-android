package com.rentalgeek.android.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rentalgeek.android.R;

/**
 * @author George
 * @purpose Fourtht slide introduction
 */
public class FragmentTutorialApplicationInstructions extends Fragment {

    public static FragmentTutorialApplicationInstructions newInstance() {
        FragmentTutorialApplicationInstructions fragment = new FragmentTutorialApplicationInstructions();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tutorial_appinstructions, container, false);

        //volley=new VolleyForAll(getActivity(), this);

        return v;

    }


}
