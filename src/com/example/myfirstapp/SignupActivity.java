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
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

public class SignupActivity extends Activity {
	private ProgressDialog progress;
	private static final String SEARCH_URL = "url";
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private static final String USER_INFO = "info";
	private static final String AUTH_TOKEN = "api_key";
	private static final String MEMBERSHIP_NO = "membership_no";
	private String SUCC = "false";

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinner_layout);
		
		progress = new ProgressDialog(this);
        progress.show();
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		System.out.println("score");
		Intent signup = getIntent();
		String numb = signup.getStringExtra(SEARCH_URL);
		
//		final TextView mTextView = (TextView) findViewById(R.id.textView5);
//		mTextView.setText(numb.toString());

		String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + numb;
		System.out.println("score here"+ numb);
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		
		try { // getting JSON string from URL
			  JSONObject json = jParser.getJSONFromUrl(url);
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
				  editor.putString("NUMBER", numb);
				  editor.putString("MEMBERSHIP_NO", membership_no);
				  editor.putString("DATE_OF_SIGNUP", data_of_signup);
				  System.out.println("im commiting");
				  editor.commit();
			  }else{
				  Toast toast = Toast.makeText(this,"      Login Failed \nPlease Try Again Later",Toast.LENGTH_LONG);
				  toast.setGravity(Gravity.TOP, 0, 170);
				  toast.show();
			  }
				  
				  
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (SUCC.equals("true")){
			progress.setMessage("Loading");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progress.setIndeterminate(true);
	        progress.show();
			Intent frontpage = new Intent(getApplicationContext(), FrontPage.class);
			frontpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			frontpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(frontpage);
		}else{
			finish();
		}

	}
	
}
