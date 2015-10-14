package com.rentalgeek.android.ui.adapter;

import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.ui.fragment.FragmentTutorialCosigner;
import com.rentalgeek.android.ui.fragment.FragmentTutorialGeekScore;
import com.rentalgeek.android.ui.fragment.FragmentTutorialRoommates;

public class SwipeAdapter extends FragmentPagerAdapter {
    
    private String currentFragmentTag;

	public SwipeAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
			case 0:
				return FragmentTutorialGeekScore.newInstance();
			case 1:
				return FragmentTutorialCosigner.newInstance();
			case 2:
				return FragmentTutorialRoommates.newInstance();
			case 3:
				return FragmentSignIn.newInstance();
			default:
				return null;
		}
	}
    
    public String getCurrentFragmentTag() {
        return currentFragmentTag;
    }

	@Override
	public int getCount() {
		return 4;
	}

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        
        Fragment fragment = (Fragment)object;

        currentFragmentTag = fragment.getTag();
        
        super.setPrimaryItem(container,position,object);   
    }

}
