package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AndroidTabLayoutActivity extends FragmentActivity implements ActionBar.TabListener {
	private ProgressDialog progress;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "My Wish list", "Top Rental", "New Arrival","Currently Reading"};
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        menuInflater.inflate(R.menu.list_page_menu, menu);
      //return true;
      return super.onCreateOptionsMenu(menu);
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		progress = new ProgressDialog(this);
		progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
		int itemId = item.getItemId();
		if (itemId == R.id.action_back) {
			// Single menu item is selected do something
          // Ex: launching new activity/screen or show alert message
          finish();
			return true;
		} else if (itemId == R.id.action_search) {
			Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
  		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_place) {
			Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
  		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_help) {
			Intent about = new Intent(getApplicationContext(), HelpActivity.class);
  		startActivity(about);
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	  }
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progress = new ProgressDialog(this);
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}
		
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	// on tab selected
	// show respected fragment view
	viewPager.setCurrentItem(tab.getPosition());
	viewPager.setOffscreenPageLimit(4);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	@Override
	public void onResume(){
		super.onResume();
	    progress.hide();
	}

}
