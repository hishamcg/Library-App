package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MyMap extends ListActivity {
    // Google Map
    //ListView listView ;
	final String[] somthing = new String[64];
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.map_list_view);
        //listView = (ListView) findViewById(R.id.map_list);
        double latitude=0;
        double longitude=0;
     
        gps = new GPSTracker(MyMap.this);
        
        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            latitude = gps.getLatitude();
            longitude = gps.getLongitude(); 
            // \n is for new line
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
        try {
            // Loading map
            //initilizeMap();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(getAssets().open(text)));
            ArrayList<String> values = new ArrayList<String>();
            String line = bReader.readLine();
            while (line != null) {
                values.add(line);
                line = bReader.readLine();
                }
            bReader.close();
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
            //final String[] somthing=new String[values.size()];
            String[] showval=new String[values.size()];
            
            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
            for (int i=0;i<dist.length;i++)
            {somthing[i] = String.valueOf(dist[i][0])+";"+String.valueOf(dist[i][1]) ;
             String[] brooo = String.valueOf(dist[i][0]).split(",");
             showval[i] = brooo[0];
             list.add(putData(brooo[0], "distance: "+String.valueOf(dist[i][1])+" meters"));
             System.out.println(somthing[i]);}
            
            String[] from = { "name", "purpose" };
            int[] to = { android.R.id.text1, android.R.id.text2 };

            SimpleAdapter adapter = new SimpleAdapter(this, list,
                    android.R.layout.simple_list_item_2, from, to);
            setListAdapter(adapter);
                
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, android.R.id.text1, showval);
            
            //listView.setAdapter(adapter);
            //listView.setOnItemClickListener(new OnItemClickListener() {

            /*    @Override
                public void onItemClick(AdapterView<?> parent, View view,
                   int position, long id) {
                  
                 // ListView Clicked item index
                 int itemPosition     = position;
                 
                 // ListView Clicked item value
                 String itemValue = somthing[position];
                 //String  itemValue    = (String) listView.getItemAtPosition(position);
                 
                  // Show Alert 
                  Toast.makeText(getApplicationContext(),
                    "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                    .show();
                 Intent signup = new Intent(getApplicationContext(), MapListView.class);
          		 signup.putExtra("click_val", itemValue);
          		 startActivity(signup);
               
                }

           }); */
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    public void onListItemClick(ListView l, View view, int position, long id) {
    	// ListView Clicked item index
        int itemPosition     = position;
        
        // ListView Clicked item value
        String itemValue = somthing[position];
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

