package com.example.myfirstapp;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

public class PageZero extends Activity {
	private ProgressDialog progress;
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private String SUCC = "false";
	private static int SPLASH_TIME_OUT = 500;
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_zero);
        progress = new ProgressDialog(this);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        
        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        //SharedPreferences value = getPreferences(MODE_PRIVATE);
		final String dateOfSignup = value.getString("DATE_OF_SIGNUP","");
		final String numb = value.getString("NUMBER", "");
		final JSONParser jParser = new JSONParser();
		
		final long saveddatevalue = new Date().getTime();
		System.out.println("here"+saveddatevalue);
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
			
            @Override
            public void run() {
            	if (numb != null && numb != ""){
            		
        			//long date_last_signup = Long.valueOf(date_of_signup).longValue();
        			try {
        				String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + numb;
        				JSONObject json = jParser.getJSONFromUrl(url);
        				SUCC = json.getString(SUCCESS);
        				System.out.println("############"+SUCC);
        		        long date_last_signup = Long.parseLong(dateOfSignup);
        				long diff =saveddatevalue - date_last_signup;
        				
        			    if(SUCC.equals("false") || diff > 25920000){
        			    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        				    SharedPreferences.Editor   editor = preferences.edit();
        				    editor.putString("AUTH_TOKEN", "0");
        				    editor.putString("MEMBERSHIP_NO", "0");
        				    editor.putString("DATE_OF_SIGNUP", "0");
        				    editor.commit();
        				    System.out.println("am i supposed to be here");
        				    
        				    progress.setMessage("Loading");
        			        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        			        progress.setIndeterminate(true);
        			        progress.show();
        			        
        				    Intent login = new Intent(getApplicationContext(), SignupPage.class);
        				    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        				    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	        		startActivity(login);
        				    }
        			    else{
        					progress.setMessage("Loading");
        			        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        			        progress.setIndeterminate(true);
        			        progress.show();
        			    	Intent in = new Intent(getApplicationContext(), FrontPage.class);
        			    	in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        			    	in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	        		startActivity(in);
        			    }
        		      } catch (NumberFormatException nfe) {
        		         System.out.println("NumberFormatException: " + nfe.getMessage());
        		      } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

        		}
        		else{
        			Intent login = new Intent(getApplicationContext(), SignupPage.class);
        		    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            		startActivity(login);
        		}
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
