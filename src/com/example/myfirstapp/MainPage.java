package com.example.myfirstapp;

import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {
	private ProgressDialog progress;
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private static final String USER_INFO = "info";
	private static final String AUTH_TOKEN = "api_key";
	private static final String MEMBERSHIP_NO = "membership_no";
	private String SUCC = "false";
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.login_menu, menu);
    //return true;
    return super.onCreateOptionsMenu(menu);
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_back) {
			finish();
			return true;
		} else if (itemId == R.id.action_help) {
			Intent about = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(about);
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	  }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        progress = new ProgressDialog(this);
        
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
        String number = tm.getLine1Number();
        
        final Button my_Button = (Button) findViewById(R.id.button1);
        final TextView no_connection = (TextView) findViewById(R.id.no_connection);
        final EditText my_numb = (EditText) findViewById(R.id.editText1);
        no_connection.setVisibility(View.GONE);
        if (number != null){
        my_numb.setText(number.replace("+91", ""));
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
        if (isNetworkAvailable()){
	        my_Button.setOnClickListener(new Button.OnClickListener() {
	        	@SuppressLint("InlinedApi")
				public void onClick(View v){
//	        		progress.setMessage("Loading...");
//	    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	    	        progress.setIndeterminate(true);
//	    	        progress.show();
	        		String phone_no = my_numb.getText().toString();
	        		Log.i("phone_no", phone_no);
	        		Toast.makeText(getApplicationContext(), "connnecting to server...",
	    	        Toast.LENGTH_SHORT).show();
	        		

	        		
	        		Random r = new Random();
        	        int pas = r.nextInt(10000 - 1000) + 1000;
	        		
	        		String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + phone_no+"&otp="+String.valueOf(pas);
	        		System.out.println("score here"+ phone_no);
	        		// Creating JSON Parser instance
	        		JSONParser jParser = new JSONParser();
	      			JSONObject json = jParser.getJSONFromUrl(url);
	        		if (json != null){
		        		try { // getting JSON string from URL
		      			  SUCC = json.getString(SUCCESS);
		      			  System.out.println(SUCC);
		      			  
		      			  if (SUCC.equals("true") ){
		      				  String INFO = json.getString(USER_INFO);
		      				  System.out.println(INFO);
		      				  JSONObject DATA = json.getJSONObject("data");
		      				  String auth_token = DATA.getString(AUTH_TOKEN);
		      				  String membership_no = DATA.getString(MEMBERSHIP_NO);
		      				  
		      				  long datevalue = new Date().getTime();
		      				  System.out.println("the date value is "+datevalue);
		      			      String data_of_signup = String.valueOf(datevalue);
		      			      
		      			      SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		      			      SharedPreferences.Editor   editor = preferences.edit();
		      				  //SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
		      				  editor.putString("AUTH_TOKEN", auth_token);
		      				  editor.putString("NUMBER", phone_no);
		      				  editor.putString("MEMBERSHIP_NO", membership_no);
		      				  editor.putString("DATE_OF_SIGNUP", data_of_signup);
		      				  System.out.println("im commiting");
		      				  editor.commit();
		      				  //------------------------------
		      				  //comment to bypass authentication via sms
		      				  Intent checking_auth = new Intent(getApplicationContext(), PageZero.class);
			      			  //Intent checking_auth = new Intent(getApplicationContext(), SignInWaitingActivity.class);
		        	          checking_auth.putExtra("pas_rand", String.valueOf(pas));
			        	      startActivity(checking_auth);
		      			  }else{
		      				  Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
		      				  toast.setGravity(Gravity.TOP, 0, 170);
		      				  toast.show();
		      				  finish();
		      			  }
		      				  
		      				  
			      		} catch (JSONException e) {
			      			e.printStackTrace();
			      			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
		      				toast.setGravity(Gravity.TOP, 0, 170);
		      				toast.show();
		      				finish();
			      		}
	        		}else{
	        			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
	      				toast.setGravity(Gravity.TOP, 0, 170);
	      				toast.show();
	      				finish();
	        		}
//	        		if (SUCC.equals("true")){
//	        			Intent checking_auth = new Intent(getApplicationContext(), SignInWaitingActivity.class);
//        	            checking_auth.putExtra("pas_rand", String.valueOf(pas));
//	        			startActivity(checking_auth);
//	        	      
//	        		}else{
//	        			finish();
//	        		}
	        		
	        	}
	        });
        }
        else {
        	no_connection.setVisibility(View.VISIBLE);
        	Toast.makeText(getApplicationContext(), "NO internet Connection!",
		    Toast.LENGTH_LONG).show();
        }
    }
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public void onDestroy() {
	    super.onDestroy();
	}
    
}
