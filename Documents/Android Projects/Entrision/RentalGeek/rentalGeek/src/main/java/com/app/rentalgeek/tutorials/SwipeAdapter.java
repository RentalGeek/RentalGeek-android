package com.app.rentalgeek.tutorials;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


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
			return FirstClass.newInstance();

		case 1: // Fragment # 0 - This will show FirstFragment different title
			return SecondClass.newInstance();

		case 2: // Fragment # 1 - This will show SecondFragment
			return ThirdClass.newInstance();

		case 3: // Fragment # 1 - This will show SecondFragment
			return SignIn.newInstance();

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
