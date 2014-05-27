package com.example.myfirstapp;

import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FrontPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        return true;
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menu_bookmark) {
			// Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            Toast.makeText(FrontPage.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
			return true;
		} else if (itemId == R.id.menu_save) {
			Toast.makeText(FrontPage.this, "Save is Selected", Toast.LENGTH_SHORT).show();
			return true;
		} else if (itemId == R.id.menu_search) {
			Toast.makeText(FrontPage.this, "Search is Selected", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	  } 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);
        progress = new ProgressDialog(this);
        progress.hide();
        final Button my_Button2 = (Button) findViewById(R.id.button2);
        final Button my_Button3 = (Button) findViewById(R.id.button3);
        final Button my_Button4 = (Button) findViewById(R.id.button4);
        final Button my_Button5 = (Button) findViewById(R.id.button5);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
        final TableRow tablerow=(TableRow) findViewById(R.id.tablerow1);
        final TextView textView = (TextView) findViewById(R.id.textView1);
        my_Button2.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        tablerow.setVisibility(View.GONE);
//        final EditText my_Text = (EditText) findViewById(R.id.editText1);
//        final String fin = my_Text.getText().toString();
        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        //SharedPreferences value = getPreferences(MODE_PRIVATE);
		  String memb = value.getString("MEMBERSHIP_NO","");
		  String authToken = value.getString("AUTH_TOKEN","");
		  String dateOfSignup = value.getString("DATE_OF_SIGNUP","");
		textView.setText(memb);
		long saveddatevalue = new Date().getTime();
		System.out.println("here"+saveddatevalue);

		if (dateOfSignup != null){
			System.out.println(saveddatevalue);
			//long date_last_signup = Long.valueOf(date_of_signup).longValue();
			try {
		        long date_last_signup = Long.parseLong(dateOfSignup);
				long diff =saveddatevalue - date_last_signup;
				
			    if(diff > 2000){
			    	SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
				    SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("AUTH_TOKEN", "0");
				    editor.putString("MEMBERSHIP_NO", "0");
				    editor.putString("DATE_OF_SIGNUP", "0");
				    editor.commit();
				    my_Button2.setVisibility(View.VISIBLE);
				    layout.setVisibility(View.GONE);
				    tablerow.setVisibility(View.GONE);
					System.out.println("value zero commited");}
			    else{
			    	layout.setVisibility(View.VISIBLE);
			    	tablerow.setVisibility(View.VISIBLE);
			    }
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
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		//searchlib.putExtra(SEARCH_URL, searchText);
        		startActivity(searchlib);
        	}
        });
        my_Button3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		String searchText = "wasssaaaap";
        		Log.i("searchTExt", searchText);
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
        		searchlib.putExtra(SEARCH_URL, searchText);
        		
        		startActivity(searchlib);
        	}
        });
        
        my_Button4.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		String searchText = "wasssaaaap";
        		Log.i("searchTExt", searchText);
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
        		startActivity(searchlib);
        	}
        });
        
        my_Button5.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
        		startActivity(searchlib);
        	}
        });
        progress.hide();
    }
    
}
