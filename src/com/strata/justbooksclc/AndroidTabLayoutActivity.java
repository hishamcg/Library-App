package com.strata.justbooksclc;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.model.NavDrawerItem;



public class AndroidTabLayoutActivity extends FragmentActivity implements ActionBar.TabListener {
	private ProgressDialog progress;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	//for drawer
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinear;
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
    private String numb;
    int sl;
    String email;
	// Tab titles
	private String[] tabs = {"Your Next Read", "New Arrival", "Top Rental"};
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater menuInflater = getMenuInflater();
	      menuInflater.inflate(R.menu.list_view, menu);
	      MenuItem overflow = menu.findItem(R.id.action_overflow);
	      MenuItem user = menu.findItem(R.id.action_user);
	      user.setTitle(email);
	      if (sl == 0){
	    	  MenuItem mg = menu.findItem(R.id.action_guru);
	    	  mg.setIcon(R.drawable.ic_lock);
	          MenuItem mp = menu.findItem(R.id.action_pro);
	          mp.setIcon(R.drawable.ic_lock);
	          MenuItem mr = menu.findItem(R.id.action_rookie);
	          mr.setIcon(R.drawable.ic_lock);
	      }else if (sl == 1){
	    	  MenuItem mg = menu.findItem(R.id.action_guru);
	    	  mg.setIcon(R.drawable.ic_lock);
	          MenuItem mp = menu.findItem(R.id.action_pro);
	          mp.setIcon(R.drawable.ic_lock);
	      }else if (sl == 2){
	    	  MenuItem mg = menu.findItem(R.id.action_guru);
	    	  mg.setIcon(R.drawable.ic_lock);
	      }
	      
	      if (numb != null && numb != "") {           
	    	  overflow.setVisible(true);
	      }else{
	    	  overflow.setVisible(false);
	      }
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
	  @SuppressWarnings("deprecation")
	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		  	
		    if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
		    mDrawerLayout.closeDrawer(mDrawerLinear);
		    int itemId = item.getItemId();
		    if (itemId == R.id.action_search) {
		      return true;
		    }else if (itemId == R.id.action_policy ){
		    	Intent policy = new Intent(getApplicationContext(), Policy.class);
		        startActivity(policy);
		    	return true;
		    }else if (itemId == R.id.shelf_view ){
		    	Intent searchlib = new Intent(getApplicationContext(), FrontPage.class);
				searchlib.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		        startActivity(searchlib);
		    	return true;
		    }else if (itemId == R.id.action_address ){
		    	Intent profile = new Intent(getApplicationContext(), ProfilePage.class);
		        startActivity(profile);
		    	return true;
		    }else if (itemId == R.id.action_help_me){
		    	Intent bookband = new Intent(getApplicationContext(), BookBand.class);
		        startActivity(bookband);
		    	return true;
		    }else if (itemId == R.id.action_dull){
		    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		        SharedPreferences.Editor   editor = preferences.edit();
		        editor.putString("MY_THEME", "");
			    editor.commit();
	    		finish();
			    Intent in = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
			    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    		startActivity(in);
		    	return true;
		    }else if (itemId == R.id.action_newbie){
		    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		        SharedPreferences.Editor   editor = preferences.edit();
		        editor.putString("MY_THEME", "green");
			    editor.commit();
	    		finish();
			    Intent in = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
			    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    		startActivity(in);
		    	return true;
		    }else if (itemId == R.id.action_rookie){
		    	if (sl >=1){
		    		SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			        SharedPreferences.Editor   editor = preferences.edit();
			        editor.putString("MY_THEME", "brown");
				    editor.commit();
		    		finish();
				    Intent in = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
				    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    		startActivity(in);
		    	}else {
		    		final AlertDialog alert = new AlertDialog.Builder(AndroidTabLayoutActivity.this).create();
	    	        alert.setTitle("Skill Level");
	    	        alert.setMessage("Your need a skill score above 50 to unlock this theme.");
	    	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int which) {
	    	        	   alert.cancel();
	    	           }
	    	        });
	    	        alert.setIcon(R.drawable.skill_level);
	    	        alert.show();
		    	}
		    	return true;
		    }else if (itemId == R.id.action_pro){
	    		if (sl >=2){
	    			SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
	    	        SharedPreferences.Editor   editor = preferences.edit();
	    	        editor.putString("MY_THEME", "violet");
	    		    editor.commit();
	        		finish();
	    		    Intent in = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
	    		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        		startActivity(in);
		    	}else {
		    		final AlertDialog alert = new AlertDialog.Builder(AndroidTabLayoutActivity.this).create();
	    	        alert.setTitle("Skill Level");
	    	        alert.setMessage("Your need a skill score above 250 to unlock this theme.");
	    	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int which) {
	    	        	   alert.cancel();
	    	           }
	    	        });
	    	        alert.setIcon(R.drawable.skill_level);
	    	        alert.show();
		    	}
		    	return true;
		    }else if (itemId == R.id.action_guru){
		    	
	    		if (sl ==3){
	    			SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
	    	        SharedPreferences.Editor   editor = preferences.edit();
	    	        editor.putString("MY_THEME", "blue");
	    		    editor.commit();
	        		finish();
	    		    Intent in = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
	    		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        		startActivity(in);
		    	}else {
		    		final AlertDialog alert = new AlertDialog.Builder(AndroidTabLayoutActivity.this).create();
	    	        alert.setTitle("Skill Level");
	    	        alert.setMessage("Your need a skill score above 500 to unlock this theme.");
	    	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int which) {
	    	        	   alert.cancel();
	    	           }
	    	        });
	    	        alert.setIcon(R.drawable.skill_level);
	    	        alert.show();
		    	}
		    	return true;
		    }
		    else {
		      return super.onOptionsItemSelected(item);
		    }
	    }

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		numb = value.getString("NUMBER","");
		sl = value.getInt("SKILL_LEVEL",0);
		String my_theme = value.getString("MY_THEME", "");
		email = value.getString("EMAIL", "guest@email.com");
			
		if (my_theme.equals("green"))
			setTheme(R.style.MyThemeGreen);
		else if (my_theme.equals("brown"))
			setTheme(R.style.MyThemeBrown);
		else if (my_theme.equals("violet"))
			setTheme(R.style.MyThemeViolet);
		else if (my_theme.equals("blue"))
			setTheme(R.style.MyThemeBlue);
		else
			setTheme(R.style.MyTheme);
		
		setContentView(R.layout.main);
		progress = new ProgressDialog(this);
		
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
		//drawer
		mTitle = mDrawerTitle = getTitle();
		if (numb != null && numb != ""){
			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		}else{
			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_non_user);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons_non_user);
		}
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tab);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mDrawerLinear = (LinearLayout) findViewById(R.id.linear_slide);
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
		
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
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
		/*Intent in = getIntent();
		String pos = null;
		pos = in.getStringExtra("list_view_active");
		if (pos!=null){
			viewPager.setCurrentItem(2);
		}*/
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
		viewPager.setOffscreenPageLimit(2);
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
  		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
  			if (numb != null && numb != ""){
  				displayView(position);
  			}else{
  				displayViewNonUser(position);
  			}
		}
  	}
  	private void displayView(int position) {
		// update the main content by replacing fragments
		//Fragment fragment = null;
		switch (position) {
		case 0:
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
			Intent tab = new Intent(getApplicationContext(), AndroidTabMyListActivity.class);
			tab.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(tab);
			
	        mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 2:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from", "front_page");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerLinear);
	  		break;
		case 3:
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 4:
			SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("AUTH_TOKEN", "");
		    editor.putString("MEMBERSHIP_NO", "");
		    editor.putString("DATE_OF_SIGNUP", "");
		    editor.putString("NUMBER", "");
		    editor.putString("regId", "");
		    editor.putString("BOOK_BAND", "");
		    editor.putString("MY_THEME", "");
		    editor.commit();
		    finish();
		    
		    Intent logout = new Intent(getApplicationContext(),PageZero.class);
		    logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    startActivity(logout);
		    mDrawerLayout.closeDrawer(mDrawerLinear);
			break;

		default:
			break;
		}

	}
  	private void displayViewNonUser(int position) {
		switch (position) {
		case 0:
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from", "front_page");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerLinear);
	  		break;
		case 2:
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 3:
			Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
    		startActivity(searchlib);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 4:
			Intent sign_up_call = new Intent(getApplicationContext(), HelpActivity.class);
    		startActivity(sign_up_call);
		    mDrawerLayout.closeDrawer(mDrawerLinear);
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
