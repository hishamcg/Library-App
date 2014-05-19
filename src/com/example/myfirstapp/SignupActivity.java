package com.example.myfirstapp;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class SignupActivity extends Activity {

	private static final String SEARCH_URL = "url";
	private static final String SERVER_BASE_URL = "staging.justbooksclc.com:8787";
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private static final String USER_INFO = "info";
	private static final String AUTH_TOKEN = "auth_token";
	private static final String MEMBERSHIP_NO = "membership_no";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front_page);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		System.out.println("score");
		Intent signup = getIntent();
		String numb = signup.getStringExtra(SEARCH_URL);
		
//		final TextView mTextView = (TextView) findViewById(R.id.textView5);
//		mTextView.setText(numb.toString());

		String url = "http://"+SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + numb;
		System.out.println("score here"+ numb);
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);
		System.out.println("sahi");
		try {
			// Getting Array of Contacts
			  String NUMBER = json.getString(SUCCESS);
			  System.out.println(NUMBER);
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
			  
			  SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			  //String token = prfs.getString("apptoken", "");
			  //SharedPreferences value = getPreferences(MODE_PRIVATE);
			  String memb = value.getString("MEMBERSHIP_NO","");
			  System.out.println("memb no is : "+memb);
			  String auth = value.getString("AUTH_TOKEN","");
			  System.out.println("auth value is : "+auth);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Intent frontpage = new Intent(getApplicationContext(), FrontPage.class);
		//searchlib.putExtra(SEARCH_URL, searchText);
		
		startActivity(frontpage);
//		try {
//		    FileOutputStream fos = openFileOutput("ContactData", Context.MODE_PRIVATE);
//		    fos.write(NUMBER.getBytes());
//		    fos.close();
//		} catch (Exception e) {
//		    e.printStackTrace();
//		}
//		
	}

}
