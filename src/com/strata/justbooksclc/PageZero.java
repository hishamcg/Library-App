package com.strata.justbooksclc;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.newrelic.agent.android.NewRelic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	private JSONParse json_parse = new JSONParse();
	RegisterActivity register_activity = new RegisterActivity();
	String memb;
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //settingup new relic
      	NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
        setContentView(R.layout.page_zero);
        //setting font
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts1/opensans.ttf");
        //FontsOverride.setDefaultFont(this);
        //to receive data from notification
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Log.i( "dd","-----------Extra:" + extras.getString("title_id") );
        }        
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		memb = value.getString("MEMBERSHIP_NO", "");
		
		if (isNetworkAvailable()){
			//checking if the user mobile has an android version above 3
			if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.HONEYCOMB){
				//here i'm checking if the user has already signed in or not
				//data from shared pref (NUMBER,AUTH_TOKEN,MEMBERSHIP_NO,DATE_OF_SIGNUP)
		    	if (memb != null && memb != ""){
		    		//for now commenting to check new layout
		    		final SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
		    		String registrationId = pref.getString(REG_ID, "");
		    		int registeredVersion = pref.getInt(APP_VERSION, Integer.MIN_VALUE);
		    		int currentVersion = getAppVersion(getApplicationContext());
		    		if (registrationId.isEmpty() || registeredVersion != currentVersion) {
		    			register_activity.start_registration(getApplicationContext());
		    		}
		    		json_parse.execute();
				}else{
					SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
				    SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("LOGIN_STATUS", "non_user");
				    editor.commit();
					Intent in = new Intent(getApplicationContext(), FrontPage.class);
				    //editor.commit();
	        		startActivity(in);
				}
			}else{
				AlertDialog alert = new AlertDialog.Builder(PageZero.this).create();
		        alert.setTitle("Alert!!");
		        alert.setMessage("Sorry this app requires android version 3.0 or above");
		        alert.setButton2("EXIT",new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		                finish();
		            }
		        });
		        // Set the Icon for the Dialog
		        alert.setIcon(R.drawable.gcm_icon);
		        alert.show();
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
	        
			String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?membership_no=" + memb;
			JSONParser jp = new JSONParser();
			JSONObject json = jp.getJSONFromUrl(url);
			return json;
		}
		@SuppressWarnings("deprecation")
		protected void onPostExecute(JSONObject json){
			String expiry_date = null;
			int books_returns_count = 0;
			int my_version = 0;
			int new_version = 0;
			//String user_name = "NA";
			String user_full_name = "NA";
			String address = "NA";
			String locality = "NA";
			String state ="NA";
			String city ="NA";
			String pincode="NA";
			String email="NA";
			String plan ="NA";
			String branch ="NA";
			//SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			//final String dateOfSignup = value.getString("DATE_OF_SIGNUP","");
			//final long saveddatevalue = new Date().getTime();
			//checking if the user is valid.
			if (json != null){
				try {
					SUCC = json.getString(SUCCESS);
					JSONObject DATA = json.getJSONObject("data");
					JSONObject USER_DATA = DATA.getJSONObject("user_data");
					
					email = USER_DATA.getString("email");
					expiry_date = DATA.getString("exp");
					books_returns_count = Integer.parseInt(DATA.getString("book_returns_count"));
					//books_returns_count = 600;
					user_full_name = DATA.getString("first_name")+"__,__"+DATA.getString("middle_name")+"__,__"+DATA.getString("last_name");
//					
					branch = DATA.getString("branch_id");
					address = DATA.getString("address");
					locality = DATA.getString("locality");
					state = DATA.getString("state");
					city = DATA.getString("city");
					pincode = DATA.getString("pincode");
					plan = DATA.getString("plan_name");
					
					if (address.equals("null"))address = "NA";
					if (locality.equals("null"))locality = "NA";
					if (state.equals("null"))state = "NA";
					if (city.equals("null"))city = "NA";
					if (pincode.equals("null"))pincode = "NA";
					
						
					
					
					new_version = Integer.parseInt(DATA.getString("version"));
					my_version = Integer.parseInt(getPackageManager().getPackageInfo(getPackageName(), 0).versionName.split("\\.")[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
				//long date_last_signup = Long.parseLong(dateOfSignup);
				//long diff =saveddatevalue - date_last_signup;
				
				if (new_version > my_version){
					AlertDialog alert = new AlertDialog.Builder(PageZero.this).create();
	    	        alert.setTitle("Alert!");
	    	        alert.setMessage("This version of the app has been depricated. Update is required.");
	    	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int which) {
	    	        	   try {
	    	        		   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.strata.justbooksclc")));
			    	       }catch(Exception e) {
			    	            Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",Toast.LENGTH_LONG).show();
			    	            e.printStackTrace();
			    	       }
	    	           }
	    	        });
	    	        alert.setButton2("Exit",new DialogInterface.OnClickListener() {
	    	            public void onClick(DialogInterface dialog, int id) {
	    	                finish();
	    	            }
	    	        });
	    	        // Set the Icon for the Dialog
	    	        alert.setIcon(R.drawable.gcm_cloud);
	    	        alert.setCancelable(false);
	    	        alert.show();
				}else{
				    if(SUCC.equals("true")){
				    	Calendar c = Calendar.getInstance(); 
				    	int day = c.get(Calendar.DAY_OF_MONTH);
				    	int month = c.get(Calendar.MONTH);
				    	int year = c.get(Calendar.YEAR);
				    	
				        String[] date = expiry_date.split("-");
				        
				        try {
				            int y1 = Integer.parseInt(date[0]);
				            int m1 = Integer.parseInt(date[1]);
				            int d1 = Integer.parseInt(date[2]);
				            String user = "exp_user";
				            if (y1 > year){
				            	user = "user";
				            }else if (y1 == year && m1 >= month){
				            	if (m1 > month){
				            		user = "user";
				            	}else if (m1 == month && d1 >= day){
				            		user = "user";
				            	}
				            }
				            
				            int value = 0;
				            String my_band = "gray";
				            if (books_returns_count < 5)
						    	value = 0;
				            else if (books_returns_count >= 5 && books_returns_count < 50){
						    	value = 1;
						    	my_band = "green";
				            }else if (books_returns_count >= 50 && books_returns_count < 250){
						    	value = 2;
						    	my_band = "brown";
						    }else if (books_returns_count >= 250 && books_returns_count < 500){
						    	value = 3;
						    	my_band = "violet";
						    }else{
						    	value = 4;
						    	my_band = "blue";}
						    
				            SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
						    SharedPreferences.Editor   editor = preferences.edit();
						    editor.putString("LOGIN_STATUS", user);
						    editor.putInt("BOOK_BAND", value);
						    editor.putString("READING_SCORE", String.valueOf(books_returns_count));
						    //editor.putString("USER_NAME",user_name );
						    editor.putString("USER_NAMES",user_full_name);
						    editor.putString("ADDRESS",address);
						    editor.putString("LOCALITY",locality);
						    editor.putString("STATE",state);
						    editor.putString("CITY",city);
						    editor.putString("PINCODE",pincode);
						    editor.putString("EMAIL",email);
						    editor.putString("PLAN",plan);
						    editor.putString("BRANCH",branch);
						    editor.putString("MY_THEME",my_band);
						    editor.commit();
				            
				        } catch(NumberFormatException nfe) {
				        	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
						    SharedPreferences.Editor   editor = preferences.edit();
						    editor.putString("LOGIN_STATUS", "exp_user");
						    editor.commit();
				        }
				    }else{
				    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
					    SharedPreferences.Editor   editor = preferences.edit();
					    editor.putString("LOGIN_STATUS", "non_user");
					    editor.commit();
				    }
		            Intent in = new Intent(getApplicationContext(), FrontPage.class);
		    		startActivity(in);
				}
			}else{
				AlertDialog alert = new AlertDialog.Builder(PageZero.this).create();
		        alert.setTitle("Authentication Error!");
		        alert.setMessage("A problem occured while authenticating your account.\nplease logout and login again.");
		        alert.setButton("Logout", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int which) {
		        	   finish();
		        	   SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
			   		   SharedPreferences.Editor editor = pref.edit();
			   		   editor.putString("AUTH_TOKEN", "");
			   	       editor.putString("MEMBERSHIP_NO", "");
			   	       editor.putString("DATE_OF_SIGNUP", "");
			  		   editor.putString("NUMBER","");
					   editor.putString("BOOK_BAND", "");
			  		   editor.putString("MY_THEME","");
			   		   editor.commit();
			   		    
			   		   Intent logout = new Intent(getApplicationContext(),PageZero.class);
			   		   startActivity(logout);
		           }
		        });
		        // Set the Icon for the Dialog
		        alert.setIcon(R.drawable.gcm_icon);
		        alert.setCancelable(false);
		        alert.show();
			}
	        
		}
	}
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
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
