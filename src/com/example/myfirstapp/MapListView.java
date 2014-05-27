package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapListView extends Activity {
    // Google Map
    private GoogleMap googleMap;
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        double latitude=0;
        double longitude=0;
        
        Intent searchlib = getIntent();
		String finn = searchlib.getStringExtra("click_val");
		String[] gohere = finn.split(";");
		String[] andhere = gohere[0].split(",");
		
		System.out.println("blahhhhhhhhhhhhhhhhhhhh"+gohere[1]+"---"+andhere[0]+"---"+andhere[1]+"---"+andhere[2]);
		
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
     
        gps = new GPSTracker(MapListView.this);
        
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
            initilizeMap();
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
                
//            	for ( int i = 0; i < splited.length-2; ++i )
//                {
//                locat += splited[i] + " ";
//                }
                if(locat == andhere[0])
                {System.out.println("oki its working");}
	            	MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(splited[splited.length-2]), Double.parseDouble(splited[splited.length-1]))).title(locat).alpha(1f);
	            	googleMap.addMarker(marker);
	            	ind++;
             }
            //Arrays.sort(dist);
            Arrays.sort(dist, new ArrayComparator(1, true));
//            for (String[] s : dist) {
//                System.out.println(s[0] + " " + s[1]);
//            }
            //String[] SortedLatLng = new String[values.size()];
            // create marker
            // adding marker
            Marker desti = googleMap.addMarker(new MarkerOptions()
        	.position(new LatLng(Double.parseDouble(andhere[1]), Double.parseDouble(andhere[2])))
        	.title(andhere[0]).alpha(1f)
            .snippet("Go here")
            .icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            desti.showInfoWindow();
            
            Marker kiel = googleMap.addMarker(new MarkerOptions()
            .position(mylocation)
            .title("You are here")
            .snippet("HERE")
            .icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            kiel.showInfoWindow();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
    	
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
 
}



