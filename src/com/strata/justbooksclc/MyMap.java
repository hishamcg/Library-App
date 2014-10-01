package com.strata.justbooksclc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.model.NavDrawerItem;

public class MyMap extends ListActivity {
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
	  String numb;
    // Google Map
    //ListView listView ;
	private JSONParse json_parse = new JSONParse();
	String[] something; //= new String[200];
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
    JSONArray list = null;
    double latitude=0;
    double longitude=0;

    @Override
      public boolean onOptionsItemSelected(MenuItem item) {
  	    if (mDrawerToggle.onOptionsItemSelected(item)) {
  			return true;
  		}
  	    return super.onOptionsItemSelected(item);
      }
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String my_theme = value.getString("MY_THEME", "");
		numb = value.getString("NUMBER","");
			
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
        
        setContentView(R.layout.fragment_pages);
        
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
		
      //---------------------for the drawer		
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
  		mDrawerToggle.syncState();
  		mDrawerLayout.setDrawerListener(mDrawerToggle);
  //-----------------------------	
        ColorDrawable gray = new ColorDrawable(this.getResources().getColor(R.color.gray));
    	getListView().setDivider(gray);
    	getListView().setDividerHeight(1);
        
        gps = new GPSTracker(MyMap.this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        //make the device wait for the response
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	json_parse.execute();
    }
    
    private class JSONParse extends AsyncTask<String,String,JSONObject>{
	  	protected void onPreExecute(){
	  		  
	  	}
	
		@Override
		protected JSONObject doInBackground(String... params) {
			String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/store_locations.json";
	    	JSONParser jParser = new JSONParser();
	    	// getting JSON string from URL
	    	JSONObject json = jParser.getJSONFromUrl(url);
	    	return json;
		}
		
		protected void onPostExecute(JSONObject json){
			final ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
			progress_bar.setVisibility(View.GONE);
			Location tempLocal1 = new Location("ref1");
	        Location tempLocal2 = new Location("ref2");
	        tempLocal1.setLatitude(latitude);
	        tempLocal1.setLongitude(longitude);
			if (json != null && !isCancelled()){
		    	try {
		    		// Getting Array of Contacts
		    		list = json.getJSONArray("storelocation");
		    		something = new String[list.length()]; 
		    		if(list.length() != 0){
			    		ArrayList<String> values = new ArrayList<String>();
			    		// looping through All Contacts
			    		for (int i = 0; i < list.length(); i++) {
			    			JSONObject c = list.getJSONObject(i);
	
			    			// Storing each json item in variable
			    			values.add(c.getString("name")+","+c.getString("latitude")+","+c.getString("longitude"));
			    		}
			    		int ind = 0;
			            String[][] dist = new String[values.size()][2];
			            for (String v : values)
			                {String[] splited = v.split(",");
			                tempLocal2.setLatitude(Double.parseDouble(splited[splited.length-2]));
			                tempLocal2.setLongitude(Double.parseDouble(splited[splited.length-1]));
			                dist[ind][0]=v;
			                //dist[ind][1]= (String.valueOf(tempLocal2.distanceTo(tempLocal1))).split("\\.")[0];
			                dist[ind][1] = String.format( "%.2f", tempLocal2.distanceTo(tempLocal1)/1000);
			                ind++;
			             }
			            Arrays.sort(dist, new ArrayComparator(1, true));
			            //final String[] something=new String[values.size()];
			            String[] showval=new String[values.size()];
	
			            ArrayList<Map<String, String>> array_list = new ArrayList<Map<String, String>>();
			            for (int i=0;i<dist.length;i++){
			            	 something[i] = String.valueOf(dist[i][0])+";"+String.valueOf(dist[i][1]) ;
				             String[] brooo = String.valueOf(dist[i][0]).split(",");
				             showval[i] = brooo[0];
				             array_list.add(putData(brooo[0], "distance: "+dist[i][1]+" KM"));
				             System.out.println(something[i]);}
	
			            String[] from = { "name", "purpose" };
			            int[] to = { android.R.id.text1, android.R.id.text2 };
	
			            SimpleAdapter adapter = new SimpleAdapter(MyMap.this, array_list,
			                    R.layout.map_list_view, from, to);
			            //ListView lv = (ListView)findViewById(R.id.list);
						//lv.setAdapter(adapter);
			            setListAdapter(adapter);
		    		}else{
		    			Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 0, 170);
						toast.show();
		    		}
		    	} catch (JSONException e) {
		    		e.printStackTrace();
		    		Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 170);
					toast.show();
		    	}
	    	}
	    	else {
	    		Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 170);
				toast.show();
	    	}
		}
    }
    public void onListItemClick(ListView l, View view, int position, long id) {
        // ListView Clicked item value
        String itemValue = something[position];
        //String  itemValue    = (String) listView.getItemAtPosition(position);

         // Show Alert
//         Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
        Intent signup = new Intent(getApplicationContext(), MapListView.class);
 		 signup.putExtra("click_val", itemValue);
 		 startActivity(signup);
    }
    private HashMap<String, String> putData(String name, String purpose) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("purpose", purpose);
        return item;
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
  		Intent in = getIntent();
  		String from = in.getStringExtra("from");
		//Fragment fragment = null;
		switch (position) {
		case 0:
			Intent intent = new Intent(getApplicationContext(), FrontPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
	  		mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
			//fragment = new AndroidTabLayoutFragment();
			if (from.equals("front_page")){
				Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
		        startActivity(searchlib);
			}
			finish();
			
	        mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 2:
			mDrawerLayout.closeDrawer(mDrawerLinear);
	  		break;
		case 3:
			//fragment = new AboutFragment();
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
			Intent intent = new Intent(getApplicationContext(), FrontPage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
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
  	public void onDestroy(){
  	  super.onDestroy();
  	  json_parse.cancel(true);
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
class ArrayComparator implements Comparator<String[]> {
    private final int columnToSort;
    private final boolean ascending;

    public ArrayComparator(int columnToSort, boolean ascending) {
        this.columnToSort = columnToSort;
        this.ascending = ascending;
    }

    public int compare(String[] c1, String[] c2) {
        int cmp = Float.valueOf(c1[columnToSort]).compareTo(Float.valueOf(c2[columnToSort]));
        return ascending ? cmp : -cmp;
    }
}

