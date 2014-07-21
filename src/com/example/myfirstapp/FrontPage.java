package com.example.myfirstapp;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myfirstapp.adapter.NavDrawerListAdapter;
import com.example.myfirstapp.model.NavDrawerItem;

public class FrontPage extends FragmentActivity {
	
  private ProgressDialog progress;
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

  private Context mContext;
  // JSON Node names
  private static final String TAG_WISHLIST = "titles";
  private static final String TAG_AUTHOR = "author";
  private static final String TAG_CATEGORY = "category";
  private static final String TAG_PAGE = "no_of_pages";
  private static final String TAG_LANGUAGE = "language";
  private static final String TAG_TITLE = "title";
  private static final String TAG_ID = "id";
  private static final String TIMES_RENTED = "no_of_times_rented";
  private static final String AVG_READING = "avg_reading_times";
  private static final String IMAGE_URL = "image_url";
  private static final String SUMMARY = "summary";
  final String[][] myarray = new String[15][10];
  JSONArray list = null;

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        menuInflater.inflate(R.menu.activity_main_actions, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }
  @SuppressLint("InlinedApi")
  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
		return true;
	}
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
    } else if (itemId == R.id.action_storage) {
        Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
        startActivity(searchlib);
      return true;
    } else if (itemId == R.id.action_place) {
    	progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
      Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
        startActivity(searchlib);
      return true;
    } else if (itemId == R.id.action_help) {
      Intent about = new Intent(getApplicationContext(), HelpActivity.class);
        startActivity(about);
      return true;
    } else if (itemId == R.id.menu_gcm) {
      Intent gcm = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(gcm);
      return true;
    } else if (itemId == R.id.logout) {
      SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor   editor = preferences.edit();
        editor.putString("AUTH_TOKEN", "0");
        editor.putString("MEMBERSHIP_NO", "0");
        editor.putString("DATE_OF_SIGNUP", "0");
        editor.putString("NUMBER", "00000");
        editor.commit();
          
        Intent login = new Intent(getApplicationContext(), SignupPage.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
      return true;
    }else {
      return super.onOptionsItemSelected(item);
    }
    }
  	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mContext = this;
      setContentView(R.layout.front_page);
      progress = new ProgressDialog(this);
      
      mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		
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
		
      final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
      pager.setOnTouchListener(new View.OnTouchListener() {

          @Override
          public boolean onTouch(View v, MotionEvent event) {
              pager.getParent().requestDisallowInterceptTouchEvent(true);
              return false;
          }
      });
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

      SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
      String auth_token = value.getString("AUTH_TOKEN","");
      String memb = value.getString("MEMBERSHIP_NO","");
      String numb = value.getString("NUMBER","");

      System.out.println("score");
      String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
      System.out.println(url);
      // Creating JSON Parser instance
      JSONParser jParser = new JSONParser();
      // getting JSON string from URL
      JSONObject json = jParser.getJSONFromUrl(url);
      try {
    	if (json != null){
	        // Getting Array of Contacts
	        list = json.getJSONArray(TAG_WISHLIST);
	
	        // looping through All Contacts
	        for (int i = 0; i < 13; i++) {
	          JSONObject c = list.getJSONObject(i);
	
	          // Storing each json item in variable
	          
	          myarray[i][0] = c.getString(IMAGE_URL);
	          myarray[i][1] = c.getString(TAG_ID);
	          myarray[i][2] = c.getString(TIMES_RENTED);
	          myarray[i][3] = c.getString(AVG_READING);
	          myarray[i][4] = c.getString(TAG_AUTHOR);
	          myarray[i][5] = c.getString(TAG_TITLE);
	          myarray[i][6] = c.getString(TAG_CATEGORY);
	          myarray[i][7] = c.getString(TAG_PAGE);
	          myarray[i][8] = c.getString(TAG_LANGUAGE);
	          myarray[i][9] = c.getString(SUMMARY);
	        }
        }else{

        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }
  	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
  		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
  	}
  	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
	        startActivity(searchlib);
			break;
		case 1:
			fragment = new FindPeopleFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;
		case 3:
			fragment = new CommunityFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			fragment = new WhatsHotFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
  	private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
          return FirstFragment.newInstance(myarray[pos+1][0],myarray[pos+1][1],myarray[pos+1][2],myarray[pos+1][3],myarray[pos+1][4],myarray[pos+1][5],myarray[pos+1][6],myarray[pos+1][7],myarray[pos+1][8],myarray[pos+1][9]);
        }

        @Override
        public int getCount() {
            return 12;
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
  @Override
  public void onBackPressed()
  {
    finish();
  }

}
