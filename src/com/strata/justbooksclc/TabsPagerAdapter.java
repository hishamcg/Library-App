package com.strata.justbooksclc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {
//	public TabsPagerAdapter(android.support.v4.app.Fragment fragment){
//	    super(fragment.getChildFragmentManager());
//	}
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new YourNextReadFragment();
		case 1:
			// Games fragment activity
			return new NewArrivalFragment();
		case 2:
			// Games fragment activity
			return new TopRentalFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}