package com.rentalgeek.android.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rentalgeek.android.ui.fragment.FragmentSignIn;
import com.rentalgeek.android.ui.fragment.FragmentTutorialGeekLink;
import com.rentalgeek.android.ui.fragment.FragmentTutorialGeekScore;
import com.rentalgeek.android.ui.fragment.FragmentTutorialGeekVision;


public class ProfileFormAdapter extends FragmentPagerAdapter {

	public ProfileFormAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
		case 0: // Fragment # 0 - This will show FirstFragment
			return FragmentProfileFormFirst.newInstance();

		case 1: // Fragment # 0 - This will show FirstFragment different title
			return FragmentTutorialGeekScore.newInstance();

		case 2: // Fragment # 1 - This will show SecondFragment
			return FragmentTutorialGeekLink.newInstance();

		case 3: // Fragment # 1 - This will show SecondFragment
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
