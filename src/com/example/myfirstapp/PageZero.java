package com.example.myfirstapp;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PageZero extends Activity {
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private String SUCC = "false";
	private JSONParse json_parse = new JSONParse();
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_zero);
        
        //to receive data from notification
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Log.i( "dd","-----------Extra:" + extras.getString("title_id") );
        }        
		
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		final String numb = value.getString("NUMBER", "");
		
		if (isNetworkAvailable()){
			//here i'm checking if the user has already signed in or not
			//data from shared pref (NUMBER,AUTH_TOKEN,MEMBERSHIP_NO,DATE_OF_SIGNUP)
	    	if (numb != null && numb != ""){
	    		json_parse.execute();
			}
			else{
				Intent login = new Intent(getApplicationContext(), SignupPage.class);
	    		startActivity(login);
			}
		}else{
			TextView tv = (TextView) findViewById(R.id.no_connection);
			tv.setVisibility(View.VISIBLE);
			Button bt = (Button) findViewById(R.id.button1);
			bt.setVisibility(View.VISIBLE);
			
			AlertDialog alert = new AlertDialog.Builder(PageZero.this).create();
	        alert.setTitle("Internet Connection");
	        alert.setMessage("Hi this application requires\ninternet connection");
	        alert.setButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	        	   Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
	   			   startActivity(intent);  
	           }
	        });
	        alert.setButton2("BACK",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	                
	            }
	        });
	        // Set the Icon for the Dialog
	        alert.setIcon(R.drawable.gcm_icon);
	        alert.show();
			
			bt.setOnClickListener(new Button.OnClickListener(){
				public void onClick(View view){
					finish();
				}
			});
			Toast.makeText(getApplicationContext(),"No Internet Connection" , Toast.LENGTH_LONG).show();
		}
    }
	private class JSONParse extends AsyncTask<String ,String , JSONObject>{
		protected void onPreExecute(){
			super.onPreExecute();
			//keep this empty so that the pagezero.xml will be visible(kind of like flash screen)
		}
		protected JSONObject doInBackground(String... args){
			//here i'm verifying if the user profile exists in memp
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			
			SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			final String numb = value.getString("NUMBER", "");
	        
			String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + numb;
			JSONParser jp = new JSONParser();
			JSONObject json = jp.getJSONFromUrl(url);
			return json;
		}
		protected void onPostExecute(JSONObject json){
			SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			final String dateOfSignup = value.getString("DATE_OF_SIGNUP","");
			final long saveddatevalue = new Date().getTime();
			//checking if the user is valid.
			if (json != null){
				try {
					SUCC = json.getString(SUCCESS);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("############"+SUCC);
	        long date_last_signup = Long.parseLong(dateOfSignup);
			long diff =saveddatevalue - date_last_signup;
			
			//-----uncomment to bypass authentication-----
			//diff = 0;
			//SUCC = "true";
			//--------------------------------------------
			
			//two condition
			//1. does he still have justbooks membership ?
			//2. 30 days since last log in (this can be removed later)
		    if(SUCC.equals("false") || diff > 2592000000L){
		    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			    SharedPreferences.Editor   editor = preferences.edit();
			    editor.putString("AUTH_TOKEN", "0");
			    editor.putString("MEMBERSHIP_NO", "0");
			    editor.putString("DATE_OF_SIGNUP", "0");
			    editor.putString("NUMBER", "00000");
			    editor.commit();
			    System.out.println("am i supposed to be here");
		        
			    Intent login = new Intent(getApplicationContext(), SignupPage.class);
        		startActivity(login);
			    }
		    else{
		    	Intent in = new Intent(getApplicationContext(), FrontPage.class);
		    	//Intent in = new Intent(getApplicationContext(), DrawerActivity.class);
        		startActivity(in);
		    }
		}
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public void onNewIntent(Intent newIntent) {
	    this.setIntent(newIntent);
	    //to receive data from notification
	    Bundle extras = getIntent().getExtras();
        if(extras != null){
            Log.i( "dd","-----------Extra:" + extras.getString("title_id") );
        }
	    // Now getIntent() returns the updated Intent        
	}
	public void onDestroy(){
		  super.onDestroy();
		 json_parse.cancel(true);
	}
}
