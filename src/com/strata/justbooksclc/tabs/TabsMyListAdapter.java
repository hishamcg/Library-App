package com.strata.justbooksclc.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strata.justbooksclc.fragment.CurrentlyReadingFragment;
import com.strata.justbooksclc.fragment.PendingOrderFragment;
import com.strata.justbooksclc.fragment.WishlistFragment;


public class TabsMyListAdapter extends FragmentPagerAdapter {
//	public TabsPagerAdapter(android.support.v4.app.Fragment fragment){
//	    super(fragment.getChildFragmentManager());
//	}
	public TabsMyListAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new CurrentlyReadingFragment();
		case 1:
			return new WishlistFragment();
		case 2:
			return new PendingOrderFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
