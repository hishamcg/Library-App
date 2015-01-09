package com.strata.justbooksclc.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.MyApplication;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.MapAdapter;
import com.strata.justbooksclc.gps.GPSTracker;
import com.strata.justbooksclc.model.MapArray;

public class MyMap extends ListActivity {
	  String numb;
    // Google Map
    //ListView listView ;
	private JSONParse json_parse;
	private View internetDown;
	private ImageButton refresh_button;
	private ProgressBar progress;
    GPSTracker gps;
    JSONArray list = null;
    double latitude=0;
    double longitude=0;
    MapAdapter map_adapter;
    ArrayList<MapArray> map_array_list;
    ListView list_view;
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    if (itemId == android.R.id.home){
	    	finish();
	    	return true;
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
	}
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String my_theme = value.getString("MY_THEME", "");
			
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
		
        setContentView(R.layout.common_list_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        list_view = (ListView)findViewById(android.R.id.list);
        TextView emptyText = new TextView(this);
		emptyText.setText("No data to show");
        list_view.setEmptyView(emptyText);
        progress = (ProgressBar)findViewById(R.id.progress_bar);
        internetDown = findViewById(R.id.internet_down);
	  	refresh_button = (ImageButton) internetDown.findViewById(R.id.refresh_button);
	  	
	  	refresh_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				internetDown.setVisibility(View.GONE);
				json_parse = new JSONParse();
		    	json_parse.execute();
			}
		});
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);

        list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		    			Uri.parse("http://maps.google.com/maps?daddr="+map_array_list.get(position).getLatitude()
		    					+","+map_array_list.get(position).getLongitude()));
		    			startActivity(intent);
			}
		});
        gps = new GPSTracker(MyMap.this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        //make the device wait for the response
        json_parse = new JSONParse();
    	json_parse.execute();
    }

    private class JSONParse extends AsyncTask<String,String,JSONObject>{
    	protected void onPreExecute(){
  		  progress.setVisibility(View.VISIBLE);
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
			progress.setVisibility(View.GONE);
			Location tempLocal1 = new Location("ref1");
	        Location tempLocal2 = new Location("ref2");
	        tempLocal1.setLatitude(latitude);
	        tempLocal1.setLongitude(longitude);
			if (json != null && !isCancelled()){
		    	try {
		    		// Getting Array of Contacts
		    		list = json.getJSONArray("storelocation");
		    		if(list != null){
		    			map_array_list = new ArrayList<MapArray>();
			    		// looping through All Contacts
			    		for (int i = 0; i < list.length(); i++) {
			    			JSONObject c = list.getJSONObject(i);
			    			// Storing each json item in variable
			    			MapArray temp_map_array = new MapArray();
			    			temp_map_array.setName(c.getString("name"));
			    			temp_map_array.setLatitude(c.getString("latitude"));
			    			temp_map_array.setLongitude(c.getString("longitude"));
			    			temp_map_array.setAddress(c.getString("address"));
			    			temp_map_array.setPhone(c.getString("phone"));

			    			tempLocal2.setLatitude(Double.parseDouble(c.getString("latitude")));
			                tempLocal2.setLongitude(Double.parseDouble(c.getString("longitude")));
			    			temp_map_array.setDistance(tempLocal2.distanceTo(tempLocal1)/1000);
			    			map_array_list.add(temp_map_array);
			    		}
			    		Collections.sort(map_array_list, new CustomComparator());
			    		map_adapter = new MapAdapter(MyMap.this,map_array_list);
			    		list_view.setAdapter(map_adapter);
		    		}else{
		    			Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP, 0, 170);
						toast.show();
		    		}
		    	} catch (JSONException e) {
		    		e.printStackTrace();
		    		Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 170);
					toast.show();
		    	}
	    	}else {
	    		internetDown.setVisibility(View.VISIBLE);
	    	}
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
class CustomComparator implements Comparator<MapArray> {

	@Override
	public int compare(MapArray lhs, MapArray rhs) {
		return Float.compare(lhs.getDistance(),rhs.getDistance());
	}
}

