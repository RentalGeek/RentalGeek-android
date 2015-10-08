package com.rentalgeek.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.ui.fragment.FragmentTutorialCosigner;
import com.rentalgeek.android.ui.fragment.FragmentTutorialGeekScore;
import com.rentalgeek.android.ui.fragment.FragmentTutorialRoommates;

public class SwipeAdapter extends FragmentPagerAdapter {

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

	@Override
	public int getCount() {
		return 4;
	}

}
