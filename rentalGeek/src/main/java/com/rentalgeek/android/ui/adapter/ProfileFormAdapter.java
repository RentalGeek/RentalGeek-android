package com.rentalgeek.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rentalgeek.android.ui.fragment.FragmentProfileForm;


public class ProfileFormAdapter extends FragmentPagerAdapter {

    public ProfileFormAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return FragmentProfileForm.newInstance(position);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return 6;
    }

}
