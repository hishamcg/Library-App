package com.strata.justbooksclc;


import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.model.NavDrawerItem;

public class FrontPage extends FragmentActivity{
	
  //private ProgressDialog progress;
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
  private static final String REG_ID = "regId";
  private JSONParse json_parse = new JSONParse();
  private JSONParse json_parse1 = new JSONParse();
  private JSONParse json_parse2 = new JSONParse();
  //String[][] myarray = new String[50][10];
  //shake detection
  private SensorManager mSensorManager;
  private ShakeEventListener mSensorListener;
  String[][] shake_array;
  ViewPager pager;
  ViewPager pager1;
  ViewPager pager2;
  //JSONArray list = null;
  String auth_token;
  String memb;
  String email;
  String numb = "";
  String my_theme = "gray";
  int sl;//skill level
  ActionBar actionBar;
  int position_of_viewpager=0;
  int position_of_viewpager1=0;
  int position_of_viewpager2=0;
  boolean shake_ready = false;
  private DBHelper mydb = new DBHelper(this);
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
	  MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.main, menu);
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
	    }else if (itemId == R.id.list_view ){
	    	Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
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
		    Intent in = new Intent(getApplicationContext(), FrontPage.class);
		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		startActivity(in);
    		finish();
	    	return true;
	    }else if (itemId == R.id.action_newbie){
	    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
	        SharedPreferences.Editor   editor = preferences.edit();
	        editor.putString("MY_THEME", "green");
		    editor.commit();
    		finish();
		    Intent in = new Intent(getApplicationContext(), FrontPage.class);
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
			    Intent in = new Intent(getApplicationContext(), FrontPage.class);
			    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    		startActivity(in);
	    	}else {
	    		final AlertDialog alert = new AlertDialog.Builder(FrontPage.this).create();
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
    		    Intent in = new Intent(getApplicationContext(), FrontPage.class);
    		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        		startActivity(in);
	    	}else {
	    		final AlertDialog alert = new AlertDialog.Builder(FrontPage.this).create();
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
    		    Intent in = new Intent(getApplicationContext(), FrontPage.class);
    		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        		startActivity(in);
	    	}else {
	    		final AlertDialog alert = new AlertDialog.Builder(FrontPage.this).create();
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
		numb = value.getString("NUMBER", "");
		sl = value.getInt("BOOK_BAND",0);
		my_theme = value.getString("MY_THEME", "");
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
		
		setContentView(R.layout.front_page);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
		
		
		//progress = new ProgressDialog(this)
		
		//-----for shake----
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorListener = new ShakeEventListener();   

	    mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

	      public void onShake() {
	    	  if(shake_ready){
		    	/*int randInt = new Random().nextInt(position_of_viewpager);
		    	pager.setCurrentItem(randInt);
		    	randInt = new Random().nextInt(position_of_viewpager1);
		    	pager1.setCurrentItem(randInt);
		    	randInt = new Random().nextInt(position_of_viewpager2);
		    	pager2.setCurrentItem(randInt);*/
	    		if (shake_array.length > 0 ){
	    			int randInt = new Random().nextInt(shake_array.length);
	    			Intent in = new Intent(getApplicationContext(),SingleMenuItemActivity.class);
	    			in.putExtra(IMAGE_URL,shake_array[randInt][0]);
	    			in.putExtra(TAG_ID, shake_array[randInt][1]);
	    			in.putExtra(TIMES_RENTED, shake_array[randInt][2]);
	    			in.putExtra(AVG_READING,shake_array[randInt][3]);
	    			in.putExtra(TAG_AUTHOR,shake_array[randInt][4]);
	    			in.putExtra(TAG_TITLE, shake_array[randInt][5]);
	    			in.putExtra(TAG_CATEGORY, shake_array[randInt][6]);
	    			in.putExtra(TAG_PAGE, shake_array[randInt][7]);
	    			in.putExtra(TAG_LANGUAGE, shake_array[randInt][8]);
	    			in.putExtra(SUMMARY,shake_array[randInt][9]);
	    			in.putExtra("message", "create");
	    			in.putExtra("check","logged_in");
	    			startActivity(in);
	    		}
	    	  }
	      }
	    });
//---------------------for the drawer------------------------------------------------------		
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
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mDrawerLinear = (LinearLayout) findViewById(R.id.linear_slide);
		
		navDrawerItems = new ArrayList<NavDrawerItem>();
		
		for (int i=0;i < navMenuTitles.length;i++){
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
		}
		
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
//-----------------------------end----------------------------------------------------------
		int l_count = 0;
		ArrayList<String> db_list = new ArrayList<String>();
		if (numb != null && numb != ""){
			pager = (ViewPager) findViewById(R.id.viewpager);
			db_list = mydb.getAllCotacts(0);
			if (db_list != null){
				l_count = db_list.size();
				String[][] mera_array = new String[l_count][10]; 
				position_of_viewpager = l_count-1;
				for (int i=0; i < l_count; i++){
					mera_array[i] = convertStringToArray(db_list.get(i));
				}
				ProgressBar vp = (ProgressBar) findViewById(R.id.viewpager_progress);
				vp.setVisibility(View.GONE);
				MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
				my_pager_adapter.setArray(mera_array);
				my_pager_adapter.setCount(position_of_viewpager);
				pager.setAdapter(my_pager_adapter);
			}
			String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/your_next_read.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
			json_parse.execute(url,"0");
			
			pager1 = (ViewPager) findViewById(R.id.viewpager1);
			db_list = mydb.getAllCotacts(1);
			if (db_list != null){
				l_count = db_list.size();
				String[][] mera_array = new String[l_count][10]; 
				position_of_viewpager1 = l_count-1;
				for (int i=0; i < l_count; i++){
					mera_array[i] = convertStringToArray(db_list.get(i));
				}
				ProgressBar vp = (ProgressBar) findViewById(R.id.viewpager_progress1);
				vp.setVisibility(View.GONE);
				MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
				my_pager_adapter.setArray(mera_array);
				my_pager_adapter.setCount(position_of_viewpager1);
				pager1.setAdapter(my_pager_adapter);
			}
			url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;		
			json_parse1.execute(url,"1");
			
			pager2 = (ViewPager) findViewById(R.id.viewpager2);
			db_list = mydb.getAllCotacts(2);
			if (db_list != null){
				l_count = db_list.size();
				String[][] mera_array = new String[l_count][10]; 
				// saving data for shake array random book view
				shake_array = new String[l_count][10]; 
				
				position_of_viewpager2 = l_count-1;
				for (int i=0; i < l_count; i++){
					mera_array[i] = convertStringToArray(db_list.get(i));
				}
				shake_array = mera_array;
				ProgressBar vp = (ProgressBar) findViewById(R.id.viewpager_progress2);
				vp.setVisibility(View.GONE);
				MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
				my_pager_adapter.setArray(mera_array);
				my_pager_adapter.setCount(position_of_viewpager2);
				pager2.setAdapter(my_pager_adapter);
			}
			url = "http://"+Config.SERVER_BASE_URL+"/api/v1/top_rentals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
			json_parse2.execute(url,"2");
		}else{
			pager = (ViewPager) findViewById(R.id.viewpager);
			String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/your_next_read.json";
			json_parse.execute(url,"0");
			pager1 = (ViewPager) findViewById(R.id.viewpager1);
			url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json";		
			json_parse1.execute(url,"1");
			pager2 = (ViewPager) findViewById(R.id.viewpager2);
			url = "http://"+Config.SERVER_BASE_URL+"/api/v1/top_rentals.json";
			json_parse2.execute(url,"2");
		}
		
//		pager.setOnTouchListener(new View.OnTouchListener() {
//		    @Override
//		    public boolean onTouch(View v, MotionEvent event) {
//		        pager.getParent().requestDisallowInterceptTouchEvent(true);
//		        return false;
//		    }
//		});

    }
  	public static String strSeparator = "__,__";
  	public static String convertArrayToString(String[] array){
  	    String str = "";
  	    for (int i = 0;i<array.length; i++) {
  	        str = str+array[i];
  	        // Do not append comma at the end of last element
  	        if(i<array.length-1){
  	            str = str+strSeparator;
  	        }
  	    }
  	    return str;
  	}
  	public static String[] convertStringToArray(String str){
  	    String[] arr = str.split(strSeparator);
  	    return arr;
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
			String[] array_stringed = new String[50];
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
			          array_stringed[i] = convertArrayToString(myarray_temp[i]);
			        }
				} catch (JSONException e) {
//					try {
//						list = url_value.getJson().getJSONArray(TAG_WISHLIST);
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					}
				    e.printStackTrace();
				}
				if(url_value.getThread_value().equals("0")){
					position_of_viewpager = list.length()-1;
					ProgressBar vp = (ProgressBar) findViewById(R.id.viewpager_progress);
					vp.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(position_of_viewpager);
					pager.setAdapter(my_pager_adapter);
					mydb.insertAllData(array_stringed,0);
					//myarray = myarray_temp;
				}else if(url_value.getThread_value().equals("1")){
					position_of_viewpager1 = list.length()-1;
					ProgressBar vp1 = (ProgressBar) findViewById(R.id.viewpager_progress1);
					vp1.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(position_of_viewpager1);
					pager1.setAdapter(my_pager_adapter);
					mydb.insertAllData(array_stringed,1);
					final SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
					String first_time = preferences.getString("FIRST_TIME","");
			        if(first_time == null || first_time.equals("")){
						final LinearLayout cm = (LinearLayout) findViewById(R.id.check_mark_view);
						cm.setVisibility(View.VISIBLE);
						cm.setOnClickListener(new Button.OnClickListener(){
							public void onClick(View v){
								cm.setVisibility(View.GONE);
								SharedPreferences.Editor   editor = preferences.edit();
							    editor.putString("FIRST_TIME", "false");
							    editor.commit();
							}
						});
			        }
					//myarray = myarray_temp;
					//pager1.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
				}else if(url_value.getThread_value().equals("2")){
					position_of_viewpager2 = list.length()-1;
					ProgressBar vp2 = (ProgressBar) findViewById(R.id.viewpager_progress2);
					vp2.setVisibility(View.GONE);
					MyPagerAdapter my_pager_adapter = new MyPagerAdapter(getSupportFragmentManager());
					my_pager_adapter.setArray(myarray_temp);
					my_pager_adapter.setCount(position_of_viewpager2);
					pager2.setAdapter(my_pager_adapter);
					mydb.insertAllData(array_stringed,2);
					//myarray = myarray_temp;
					//pager2.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
			        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
					final String login_status = value.getString("LOGIN_STATUS", "");
					if (login_status.equals("non_user")){
//						Intent mainp = new Intent(getApplicationContext(),MainPage.class);
//						startActivity(mainp);
					}else if (login_status.equals("exp_user")){
						Intent exp = new Intent(getApplicationContext(),ExpiredPage.class);
						startActivity(exp);
					}
					shake_ready = true;
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
  			if (numb != null && numb != ""){
  				displayView(position);
  			}else{
  				displayViewNonUser(position);
  			}
		}
  	}
  	private void displayView(int position) {
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
		    editor.putString(REG_ID, "");
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
  	
  	private class MyPagerAdapter extends FragmentPagerAdapter {
  		private int count;
  		private String[][] myarray = new String[50][10];
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
          return FirstFragment.newInstance(myarray[pos+1][0],myarray[pos+1][1],myarray[pos+1][2],myarray[pos+1][3],myarray[pos+1][4],myarray[pos+1][5],myarray[pos+1][6],myarray[pos+1][7],myarray[pos+1][8],myarray[pos+1][9],my_theme);
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
      mSensorManager.registerListener(mSensorListener,
        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onPause() {
      mSensorManager.unregisterListener(mSensorListener);
      super.onPause();
    }
    public void onDestroy(){
	  super.onDestroy();
	  json_parse.cancel(true);
	  json_parse1.cancel(true);
	  json_parse2.cancel(true);
	  mydb.close();
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
    public void onBackPressed(){
    	super.onBackPressed();
    	mydb.close();
    	finish();
    }

}
