package com.strata.justbooksclc.tabs;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
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
import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.MyApplication;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.activity.BookBand;
import com.strata.justbooksclc.activity.HelpActivity;
import com.strata.justbooksclc.activity.PageZero;
import com.strata.justbooksclc.activity.Policy;
import com.strata.justbooksclc.activity.ProfilePage;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.map.MyMap;
import com.strata.justbooksclc.model.NavDrawerItem;
import com.strata.justbooksclc.signin.IntroPage;
import com.strata.justbooksclc.signin.SigninActivity;
import com.strata.justbooksclc.signin.Signup;



public class TabLayoutActivity extends FragmentActivity implements ActionBar.TabListener {
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
    private TypedArray menuThemeIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private String numb = "";
    String my_theme = "gray";
    int sl = 0;
    String email;
	// Tab titles
	private String[] tabs = {"Your Next Read", "Top Rental","New Arrival"};
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater menuInflater = getMenuInflater();
	      menuInflater.inflate(R.menu.main, menu);
	      MenuItem overflow = menu.findItem(R.id.action_overflow);
	      MenuItem user = menu.findItem(R.id.action_user);
	      MenuItem action_level = menu.findItem(R.id.action_level);
	      user.setTitle(email);
	      //changing bookband heart color according to the theme
	      setJustBooksTheme(action_level);
	      //locking the view for bookband
	      menulockBookband(menu);
	      
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
	private void setJustBooksTheme(MenuItem action_level){
		if(my_theme.equals("green"))
	    	  action_level.setIcon(R.drawable.ic_heart_green);
	      else if(my_theme.equals("brown"))
	    	  action_level.setIcon(R.drawable.ic_heart_brown);
	      else if(my_theme.equals("violet"))
	    	  action_level.setIcon(R.drawable.ic_heart_violet);
	      else if(my_theme.equals("blue"))
	    	  action_level.setIcon(R.drawable.ic_heart_blue);
	      else
	    	  action_level.setIcon(R.drawable.ic_heart_gray);
	}
	private void menulockBookband(Menu menu){
		//locking the view for bookband
	      MenuItem[] menu_array = {menu.findItem(R.id.action_newbie),
					    		  menu.findItem(R.id.action_rookie),
					    		  menu.findItem(R.id.action_pro),
					    		  menu.findItem(R.id.action_guru)};
	      for(int i=3;i>=sl;i--){
	    	  menu_array[i].setIcon(R.drawable.ic_lock);
	      }

	}
	@SuppressWarnings("deprecation")
	private void themeChange(int customer_level){
		menuThemeIcons = getResources().obtainTypedArray(R.array.themes_icon);
        		
		if (sl >= customer_level){
			SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
	        SharedPreferences.Editor   editor = preferences.edit();
	        editor.putString("MY_THEME", getResources().getStringArray(R.array.justbooks_themes_array)[customer_level]);
		    editor.commit();
			finish();
		    Intent in = new Intent(getApplicationContext(), TabLayoutActivity.class);
		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(in);
    	}else {
    		final AlertDialog alert = new AlertDialog.Builder(TabLayoutActivity.this).create();
	        alert.setTitle("Book Band");
	        alert.setMessage(getResources().getStringArray(R.array.themes_locked_message)[customer_level]);
	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	        	   alert.cancel();
	           }
	        });
	        alert.setIcon(menuThemeIcons.getResourceId(customer_level, -1));
	        alert.show();
	        menuThemeIcons.recycle();
    	}
	}
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
		    }else if (itemId == R.id.action_address ){
		    	Intent profile = new Intent(getApplicationContext(), ProfilePage.class);
		        startActivity(profile);
		    	return true;
		    }else if (itemId == R.id.action_help_me){
		    	Intent bookband = new Intent(getApplicationContext(), BookBand.class);
		        startActivity(bookband);
		    	return true;
		    }else if (itemId == R.id.action_dull && !my_theme.equals("gray")){
		    	themeChange(0);
		    	return true;
		    }else if (itemId == R.id.action_newbie && !my_theme.equals("brown")){
		    	themeChange(1);
		    	return true;
		    }else if (itemId == R.id.action_rookie && !my_theme.equals("green")){
		    	themeChange(2);
		    	return true;
		    }else if (itemId == R.id.action_pro && !my_theme.equals("violet")){
		    	themeChange(3);
		    	return true;
		    }else if (itemId == R.id.action_guru && !my_theme.equals("blue")){
		    	themeChange(4);
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
		InitializeCustomData();
		//setting up new relic
		NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
		
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		final String login_status = value.getString("LOGIN_STATUS", "");
		if (login_status.equals("non_user")){
			Intent exp = new Intent(getApplicationContext(),IntroPage.class);
			startActivity(exp);
		}
		
		setContentView(R.layout.tab_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		setUpDrawer();
			
		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),getApplicationContext());
		
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
		viewPager.setCurrentItem(1, false);
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
	
	private void InitializeCustomData(){
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		numb = value.getString("NUMBER","");
		my_theme = value.getString("MY_THEME", "");
		if (numb != null && numb != ""){
			sl = value.getInt("BOOK_BAND",0);
			email = value.getString("EMAIL", "guest@email.com");}
			
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
	}
	private void setUpDrawer(){
		//drawer
		mTitle = getTitle();
		mDrawerTitle = "Menu";
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
		
		for (int i=0;i < navMenuTitles.length;i++){
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
		}
		
		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);
		// enabling action bar app icon and behaving it as toggle button
		
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
	}
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
  		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
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
			Intent tab = new Intent(getApplicationContext(), TabMyListActivity.class);
			tab.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(tab);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
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
			Intent searchlib = new Intent(getApplicationContext(), SigninActivity.class);
    		startActivity(searchlib);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 4:
			Intent sign_up_call = new Intent(getApplicationContext(), Signup.class);
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
