package com.example.myfirstapp;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FrontPage extends Activity {
	private static final String SEARCH_URL = "url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);
        
        final Button my_Button2 = (Button) findViewById(R.id.button2);
        final Button my_Button3 = (Button) findViewById(R.id.button3);
        final Button my_Button4 = (Button) findViewById(R.id.button4);
        final Button my_Button5 = (Button) findViewById(R.id.button5);
        my_Button2.setVisibility(View.GONE);
//        final EditText my_Text = (EditText) findViewById(R.id.editText1);
//        final String fin = my_Text.getText().toString();
        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        //SharedPreferences value = getPreferences(MODE_PRIVATE);
		  String memb = value.getString("MEMBERSHIP_NO","");
		  System.out.println("front memb no is : "+memb);
		  String authToken = value.getString("AUTH_TOKEN","");
		  System.out.println("front auth value is : "+authToken);
		  String dateOfSignup = value.getString("DATE_OF_SIGNUP","");
		  System.out.println("front date is : "+dateOfSignup);
		
		System.out.println("front page auth value: "+authToken);
		System.out.println("front page date value: "+dateOfSignup);
		
		long saveddatevalue = new Date().getTime();
		System.out.println("here"+saveddatevalue);

		if (dateOfSignup != null){
			System.out.println(saveddatevalue);
			//long date_last_signup = Long.valueOf(date_of_signup).longValue();
			try {
		        long date_last_signup = Long.parseLong(dateOfSignup);
		        System.out.println("long l = " + date_last_signup);
				long diff =saveddatevalue - date_last_signup;
				System.out.println("diff between the date : "+diff);
				
			    if(diff > 2000){
			    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
				    SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("AUTH_TOKEN", "0");
				    editor.putString("MEMBERSHIP_NO", "0");
				    editor.putString("DATE_OF_SIGNUP", "0");
				    editor.commit();
				    my_Button2.setVisibility(View.VISIBLE);
					System.out.println("value zero commited");}
		      } catch (NumberFormatException nfe) {
		         System.out.println("NumberFormatException: " + nfe.getMessage());
		      }

			}
		
		
//		
//		
		
		if(authToken == null || authToken.isEmpty() ){
			my_Button2.setVisibility(View.VISIBLE);
			System.out.println("the sign up button should be visible now");
		}
	    my_Button2.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		//String searchText = "wasssaaaap";
        		//Log.i("searchTExt", searchText);
        		Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		//searchlib.putExtra(SEARCH_URL, searchText);
        		
        		startActivity(searchlib);
        	}
        });
        my_Button3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		String searchText = "wasssaaaap";
        		Log.i("searchTExt", searchText);
        		Intent searchlib = new Intent(getApplicationContext(), DefaultBookList.class);
        		searchlib.putExtra(SEARCH_URL, searchText);
        		
        		startActivity(searchlib);
        	}
        });
        
        my_Button4.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		String searchText = "wasssaaaap";
        		Log.i("searchTExt", searchText);
        		Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
        		startActivity(searchlib);
        	}
        });
        
        my_Button5.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		Intent searchlib = new Intent(getApplicationContext(), MainActivity.class);
        		searchlib.putExtra(SEARCH_URL, "a");
        		startActivity(searchlib);
        	}
        });
    }
    
}
