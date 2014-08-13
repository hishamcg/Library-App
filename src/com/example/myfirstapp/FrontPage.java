package com.example.myfirstapp;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.adapter.NavDrawerListAdapter;
import com.example.myfirstapp.model.NavDrawerItem;

public class FrontPage extends FragmentActivity {
	
  //private ProgressDialog progress;
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
  private JSONParse json_parse = new JSONParse();
  private JSONParse json_parse1 = new JSONParse();
  private JSONParse json_parse2 = new JSONParse();
  //String[][] myarray = new String[50][10];
  ViewPager pager;
  ViewPager pager1;
  ViewPager pager2;
  //JSONArray list = null;
  String auth_token;
  String memb;
  String numb;
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
	  MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.main, menu);
      
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
  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
	    int itemId = item.getItemId();
	    if (itemId == R.id.action_search) {
	        /*Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
	        startActivity(searchlib);*/
	      return true;
	    }else if (itemId == R.id.list_view){
	    	Intent list = new Intent(getApplicationContext(),AndroidTabLayoutActivity.class);
	    	list.putExtra("list_view_active", "true");
	    	startActivity(list);
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
		setContentView(R.layout.front_page);
		//progress = new ProgressDialog(this);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
//---------------------for the drawer		
		mTitle = mDrawerTitle = getTitle();
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
//-----------------------------	
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		auth_token = value.getString("AUTH_TOKEN","");
		memb = value.getString("MEMBERSHIP_NO","");
		numb = value.getString("NUMBER","");
		
		pager = (ViewPager) findViewById(R.id.viewpager);
		String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
		json_parse.execute(url,"0");
		pager1 = (ViewPager) findViewById(R.id.viewpager1);
		url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;		
		json_parse1.execute(url,"1");
		pager2 = (ViewPager) findViewById(R.id.viewpager2);
		url = "http://"+Config.SERVER_BASE_URL+"/api/v1/top_rentals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
		json_parse2.execute(url,"2");
		
		pager.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        pager.getParent().requestDisallowInterceptTouchEvent(true);
		        return false;
		    }
		});
		pager1.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        pager1.getParent().requestDisallowInterceptTouchEvent(true);
		        return false;
		    }
		});
		pager2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					pager2.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
			}
		});
    }
  	public class UrlValue {
  		private String thread_value;
  		private JSONObject json;
  		
  		public String getThread_value() {
			return thread_value;
		}
		public void setThread_value(String thread_value) {
			this.thread_value = thread_value;
		}
		public JSONObject getJson() {
			return json;
		}
		public void setJson(JSONObject json) {
			this.json = json;
		}
  	}
  	private class JSONParse extends AsyncTask<String,String,UrlValue>{
  		@SuppressLint("NewApi")
		protected void onPreExecute(){
  			
  		}
		protected UrlValue doInBackground(String... args) {
			//String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(args[0]);
			UrlValue url_value = new UrlValue();
			url_value.setThread_value(args[1]);
			url_value.setJson(json);
			return url_value;
		}
		protected void onPostExecute(UrlValue url_value){
			String[][] myarray_temp = new String[50][10];
			JSONArray list = null;
			if (url_value.getJson() != null && !isCancelled()){
				try {
			        // Getting Array of Contacts
			        list = url_value.getJson().getJSONArray(TAG_WISHLIST);
			        // looping through All Contacts
			        for (int i = 0; i < list.length(); i++) {
			          JSONObject c = list.getJSONObject(i);
			          // Storing each json item in variable
			          myarray_temp[i][0] = c.getString(IMAGE_URL);
			          myarray_temp[i][1] = c.getString(TAG_ID);
			          myarray_temp[i][2] = c.getString(TIMES_RENTED);
			          myarray_temp[i][3] = c.getString(AVG_READING);
			          myarray_temp[i][4] = c.getString(TAG_AUTHOR);
			          myarray_temp[i][5] = c.getString(TAG_TITLE);
			          myarray_temp[i][6] = c.getString(TAG_CATEGORY);
			          myarray_temp[i][7] = c.getString(TAG_PAGE);
			          myarray_temp[i][8] = c.getString(TAG_LANGUAGE);
			          myarray_temp[i][9] = c.getString(SUMMARY);
			        }
				} catch (JSONException e) {
				    e.printStackTrace();
				}
				if(url_value.getThread_value().equals("0")){
					ProgressBar vp = (ProgressBar) findViewById(R.id.viewpager_progress);
					vp.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(list.length()-1);
					pager.setAdapter(my_pager_adapter);
					//myarray = myarray_temp;
				}else if(url_value.getThread_value().equals("1")){
					ProgressBar vp1 = (ProgressBar) findViewById(R.id.viewpager_progress1);
					vp1.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(list.length()-1);
					pager1.setAdapter(my_pager_adapter);
					//myarray = myarray_temp;
					//pager1.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
				}else if(url_value.getThread_value().equals("2")){
					ProgressBar vp2 = (ProgressBar) findViewById(R.id.viewpager_progress2);
					vp2.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(list.length()-1);
					pager2.setAdapter(my_pager_adapter);
					//myarray = myarray_temp;
					//pager2.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
				}else{
					Toast.makeText(getApplicationContext(), "something went wrong.", Toast.LENGTH_LONG).show();
				}
			}
		}	
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
		// update the main content by replacing fragments
		//Fragment fragment = null;
		switch (position) {
		case 0:
			//fragment = new HomeFragment();
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 1:
			//fragment = new AndroidTabLayoutFragment();
			Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
	        startActivity(searchlib);
	        mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 2:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from", "front_page");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerList);
	  		break;
		/*case 3:
			//fragment = new RegisterFragment();
	  		Intent gcm = new Intent(getApplicationContext(),RegisterActivity.class);
	  		startActivity(gcm);
	  		mDrawerLayout.closeDrawer(mDrawerList);
			break;*/
		case 3:
			//fragment = new AboutFragment();
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerList);
			break;
		case 4:
			SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("AUTH_TOKEN", "0");
		    editor.putString("MEMBERSHIP_NO", "0");
		    editor.putString("DATE_OF_SIGNUP", "0");
		    editor.putString("NUMBER", "00000");
		    editor.commit();
		    
		    Intent logout = new Intent(getApplicationContext(),SignupPage.class);
		    startActivity(logout);
		    mDrawerLayout.closeDrawer(mDrawerList);
			break;

		default:
			break;
		}
//-----------------------
// used to display fragment
//-----------------------
		/*if (fragment != null) {
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
		}*/
	}
  	private class MyPagerAdapter extends FragmentPagerAdapter {
  		private int count;
  		private String[][] myarray = new String[50][10];
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
          return FirstFragment.newInstance(myarray[pos+1][0],myarray[pos+1][1],myarray[pos+1][2],myarray[pos+1][3],myarray[pos+1][4],myarray[pos+1][5],myarray[pos+1][6],myarray[pos+1][7],myarray[pos+1][8],myarray[pos+1][9]);
        }
        public void setCount(int count){
        	this.count = count;
        }
        public void setArray(String[][] myarray){
        	this.myarray = myarray;
        }
        @Override
        public int getCount() {
            //return list.length();
        	return count;
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
    //progress.hide();
  }
  @Override
  public void onBackPressed()
  {
    finish();
  }
  public void onDestroy(){
	  super.onDestroy();
	 json_parse.cancel(true);
  }

}
