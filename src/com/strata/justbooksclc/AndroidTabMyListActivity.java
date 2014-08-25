package com.strata.justbooksclc;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.model.NavDrawerItem;



public class AndroidTabMyListActivity extends FragmentActivity implements ActionBar.TabListener {
	private ProgressDialog progress;
	private ViewPager viewPager;
	private TabsMyListAdapter mAdapter;
	private ActionBar actionBar;
	//for drawer
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
  // nav drawer title
    private CharSequence mDrawerTitle;
  // used to store app title
    private CharSequence mTitle;
  // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
	// Tab titles
	private String[] tabs = { "My Wishlist","Pending Order","Currently Reading"};
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	  MenuInflater menuInflater = getMenuInflater();
      //menuInflater.inflate(R.menu.front_page, menu);
      menuInflater.inflate(R.menu.search_page_menu, menu);
      SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
      SearchView searchView =
                 (SearchView) menu.findItem(R.id.action_search).getActionView();
      searchView.setSearchableInfo(
                 searchManager.getSearchableInfo(getComponentName()));
      //searchView.setOnQueryTextListener(this);
      //return true;
      int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
      TextView textView = (TextView) searchView.findViewById(id);
      textView.setHintTextColor(0x88ffffff);
      textView.setTextColor(0xffffffff);
      return super.onCreateOptionsMenu(menu);
    }
  @SuppressLint("NewApi")
  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
	    mDrawerLayout.closeDrawer(mDrawerList);
	    int itemId = item.getItemId();
	    if (itemId == R.id.action_search) {
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
		
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
//-----------------------------------------------------------------------------------------------------------
		//drawer
		mTitle = mDrawerTitle = getTitle();
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tab);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		
		navDrawerItems = new ArrayList<NavDrawerItem>();
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));// Wishlist
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));// Location
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));// GCM
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));// About
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));// Log out
		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_white, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
//---------------------------------------------------------------------------------------------------		
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsMyListAdapter(getSupportFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		//actionBar.setHomeButtonEnabled(false);
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
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
  		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
  	}
  	private void displayView(int position) {
		switch (position) {
		case 0:
			finish();
	        mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 1:
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 2:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from","tab_layout");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerList);
	  		break;
		case 3:
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 4:
			SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("AUTH_TOKEN", "");
		    editor.putString("MEMBERSHIP_NO", "");
		    editor.putString("DATE_OF_SIGNUP", "");
		    editor.putString("NUMBER", "");
		    editor.commit();
		    
		    Intent logout = new Intent(getApplicationContext(),PageZero.class);
		    startActivity(logout);
		    mDrawerLayout.closeDrawer(mDrawerList);
			break;

		default:
			break;
		}

	}
  	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public void onResume(){
		super.onResume();
	    progress.hide();
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
