package com.rentalgeek.android.tutorials;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rentalgeek.android.fragment.FragmentSignIn;
import com.rentalgeek.android.fragment.FragmentTutorialGeekLink;
import com.rentalgeek.android.fragment.FragmentTutorialGeekScore;
import com.rentalgeek.android.fragment.FragmentTutorialGeekVision;


/**
 * 
 * @author George
 * 
 * @purpose Adapter class which handles the introduction Sliders
 *
 */
public class SwipeAdapter extends FragmentPagerAdapter {

	public SwipeAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: // Fragment # 0 - This will show FirstFragment
			return FragmentTutorialGeekVision.newInstance();

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
		// TODO Auto-generated method stub
		return 4;
	}

}
