package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MyMap extends ListActivity {
    // Google Map
    //ListView listView ;
	final String[] something = new String[64];
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
    JSONArray list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        double latitude=0;
        double longitude=0;

        gps = new GPSTracker(MyMap.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        Location tempLocal1 = new Location("ref1");
        Location tempLocal2 = new Location("ref2");
        tempLocal1.setLatitude(latitude);
        tempLocal1.setLongitude(longitude);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/store_locations.json";
        //setContentView(R.layout.map_list_view);
        //listView = (ListView) findViewById(R.id.map_list);
    	JSONParser jParser = new JSONParser();
    	// getting JSON string from URL
    	JSONObject json = jParser.getJSONFromUrl(url);
    	if (json != null){
	    	try {
	    		// Getting Array of Contacts
	    		list = json.getJSONArray("storelocation");
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
	                dist[ind][1]=String.valueOf(tempLocal2.distanceTo(tempLocal1));
	                ind++;
	             }
	            Arrays.sort(dist, new ArrayComparator(1, true));
	            //final String[] something=new String[values.size()];
	            String[] showval=new String[values.size()];

	            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	            for (int i=0;i<dist.length;i++)
	            {something[i] = String.valueOf(dist[i][0])+";"+String.valueOf(dist[i][1]) ;
	             String[] brooo = String.valueOf(dist[i][0]).split(",");
	             showval[i] = brooo[0];
	             list.add(putData(brooo[0], "distance: "+String.valueOf(dist[i][1])+" meters"));
	             System.out.println(something[i]);}

	            String[] from = { "name", "purpose" };
	            int[] to = { android.R.id.text1, android.R.id.text2 };

	            SimpleAdapter adapter = new SimpleAdapter(this, list,
	                    android.R.layout.simple_list_item_2, from, to);
	            setListAdapter(adapter);
	    	} catch (JSONException e) {
	    		e.printStackTrace();
	    	}
    	}
    	else {
    		Toast toast = Toast.makeText(getApplicationContext(),"Sorry no Data to Show\nThe server might be down",Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 170);
			toast.show();
    	}
    }
    public void onListItemClick(ListView l, View view, int position, long id) {
    	// ListView Clicked item index
        int itemPosition     = position;

        // ListView Clicked item value
        String itemValue = something[position];
        //String  itemValue    = (String) listView.getItemAtPosition(position);

         // Show Alert
         Toast.makeText(getApplicationContext(),
           "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
           .show();
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

