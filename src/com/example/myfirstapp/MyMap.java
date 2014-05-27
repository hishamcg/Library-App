package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMap extends Activity {
    // Google Map
    ListView listView ;
    private GoogleMap googleMap;
    private static final String DIST_ARRAY = "dist_array";
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.map_layout);
        setContentView(R.layout.map_list_view);
        listView = (ListView) findViewById(R.id.map_list);
        double latitude=0;
        double longitude=0;

//        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
     
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
        final LatLng mylocation = new LatLng(latitude,longitude);
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
                String locat;
                tempLocal2.setLatitude(Double.parseDouble(splited[splited.length-2]));
                tempLocal2.setLongitude(Double.parseDouble(splited[splited.length-1]));
                //System.out.println(splited[splited.length-2]+" "+splited[splited.length-1]);
                locat=splited[0];
                dist[ind][0]=v;
                dist[ind][1]=String.valueOf(tempLocal2.distanceTo(tempLocal1));
                //google.maps.geometry.spherical.computeDistanceBetween(tempLocal2, tempLocal1); 

                //MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(splited[splited.length-2]), Double.parseDouble(splited[splited.length-1]))).title(locat).alpha(1f);
                //googleMap.addMarker(marker);
                ind++;
             }
            //Arrays.sort(dist);
            Arrays.sort(dist, new ArrayComparator(1, true));
            String[] somthing=new String[values.size()];
            
            for (int i=0;i<dist.length;i++)
            {//somthing[i] = String.valueOf(dist[i][0])+";"+String.valueOf(dist[i][1]) ;
            	String[] brooo = String.valueOf(dist[i][0]).split(",");
            	somthing[i] = brooo[0];
            	
             System.out.println(somthing[i]);}
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, somthing);
            
            listView.setAdapter(adapter); 
            //Intent maplist = new Intent(getApplicationContext(), MapListView.class);
            //Bundle mBundle = new Bundle();
            //mBundle.putSerializable(DIST_ARRAY, dist);
            //maplist.putExtras(mBundle);
            //startActivity(maplist);
            
            
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                   int position, long id) {
                  
                 // ListView Clicked item index
                 int itemPosition     = position;
                 
                 // ListView Clicked item value
                 String  itemValue    = (String) listView.getItemAtPosition(position);
                 
                  // Show Alert 
                  Toast.makeText(getApplicationContext(),
                    "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                    .show();
                 Intent signup = new Intent(getApplicationContext(), MapListView.class);
          		 signup.putExtra("click_val", itemValue);
          		 startActivity(signup);
               
                }

           }); 
 

            // adding marker
            
//            Marker kiel = googleMap.addMarker(new MarkerOptions()
//            .position(mylocation)
//            .title("You are here")
//            .snippet("HERE")
//            .icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//            kiel.showInfoWindow();
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15));
//            // Zoom in, animating the camera.
//            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
//    private void initilizeMap() {
//      
//        if (googleMap == null) {
//            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//                    R.id.map)).getMap();
// 
//            // check if map is created successfully or not
//            if (googleMap == null) {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
 
//   @Override
//    protected void onResume() {
//        super.onResume();
//        initilizeMap();
//    }
 
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
//###########################################

