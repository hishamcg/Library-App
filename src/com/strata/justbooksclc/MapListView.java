package com.strata.justbooksclc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapListView extends FragmentActivity {
    // Google Map
    private GoogleMap googleMap;
    String text = "latlog.txt"; //your text file name in the assets folder
    GPSTracker gps;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        
        setContentView(R.layout.map_layout);
        
		//Get a Tracker (should auto-report)
		//GoogleAnalytics.getInstance(this).getLogger().setLogLevel(LogLevel.VERBOSE);
		((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
        
        double latitude=0;
        double longitude=0;
        
        Intent searchlib = getIntent();
		String finn = searchlib.getStringExtra("click_val");
		String[] gohere = finn.split(";");
		String[] andhere = gohere[0].split(",");
		
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
     
        gps = new GPSTracker(MapListView.this);
        
        // check if GPS enabled     
        if(gps.canGetLocation()){
             
            latitude = gps.getLatitude();
            longitude = gps.getLongitude(); 
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
        }
        final LatLng mylocation = new LatLng(latitude,longitude);
        final LatLng storelocation = new LatLng(Double.parseDouble(andhere[1]), Double.parseDouble(andhere[2]));
        try {
            // Loading map
            initilizeMap();
            if(gps.canGetLocation()){
	            // Getting URL to the Google Directions API
	            String url = getDirectionsUrl(storelocation, mylocation);
	
	            DownloadTask downloadTask = new DownloadTask();
	
	            // Start downloading json data from Google Directions API
	            downloadTask.execute(url);
	            
	            Marker desti = googleMap.addMarker(new MarkerOptions()
	        	.position(storelocation)
	        	.title(andhere[0]).alpha(1f)
	            .snippet("Go here")
	            .icon(BitmapDescriptorFactory
	                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	            desti.showInfoWindow();
           
            
	            googleMap.addMarker(new MarkerOptions()
	            .position(mylocation)
	            .title("You are here")
	            .snippet("HERE")
	            .icon(BitmapDescriptorFactory
	                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 1000));
	            // Zoom in, animating the camera.
	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }else{
            	googleMap.addMarker(new MarkerOptions()
	            .position(storelocation)
	            .title(andhere[0])
	            .snippet("HERE")
	            .icon(BitmapDescriptorFactory
	                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storelocation, 1000));
	            // Zoom in, animating the camera.
	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
					
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			if (isCancelled())
            	return null;
            else
				return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            if (isCancelled())
            	return null;
            else
            	return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);					
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(4);
				lineOptions.color(Color.RED);	
				
			}
			
			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);							
		}			
    }  
    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
    	
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
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
    public void onDestroy(){
    	super.onDestroy();
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



