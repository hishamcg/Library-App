package com.strata.justbooksclc.tabs;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.GlobalMenuDrawerActivity;
import com.strata.justbooksclc.MyApplication;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.activity.ExpiredPage;



public class TabMyListActivity extends GlobalMenuDrawerActivity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsMyListAdapter mAdapter;
	private ActionBar actionBar;
	
	private String[] tabs = {"Reading", "Wishlist","Pending Order"};
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
		
		setContentView(R.layout.tab_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
			
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsMyListAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
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
		viewPager.setCurrentItem(1, false);
		
		//show expired page if
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		final String login_status = value.getString("LOGIN_STATUS", "");
		if (login_status.equals("exp_user")){
			Intent exp = new Intent(getApplicationContext(),ExpiredPage.class);
			startActivity(exp);
		}
	}
	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft){
	}
	
	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		viewPager.setOffscreenPageLimit(2);
	}
	
	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	public void onStart(){
    	super.onStart();
    	//Get an Analytics tracker to report app starts &amp; uncaught exceptions etc.
    	GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }
    public void onStop(){
    	super.onStop();
    	//Stop the analytics tracking
    	GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
