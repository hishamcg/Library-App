package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_page_menu, menu);
    //return true;
    return super.onCreateOptionsMenu(menu);
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		progress = new ProgressDialog(this);
		progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
		int itemId = item.getItemId();
		if (itemId == R.id.action_back) {
			// Single menu item is selected do something
        // Ex: launching new activity/screen or show alert message
        finish();
			return true;
		} else if (itemId == R.id.action_search) {
			Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
    		searchlib.putExtra("check","not_logged_in");
    		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_place) {
			Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
			startActivity(searchlib);
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
        progress.hide();
        final Button my_Button = (Button) findViewById(R.id.button1);
        final TextView no_connection = (TextView) findViewById(R.id.no_connection);
        no_connection.setVisibility(View.GONE);
//        final EditText my_Text = (EditText) findViewById(R.id.editText1);
//        final String fin = my_Text.getText().toString();
        if (isNetworkAvailable()){
	        my_Button.setOnClickListener(new Button.OnClickListener() {
	        	public void onClick(View v){
	        		progress.setMessage("Loading...");
	    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    	        progress.setIndeterminate(true);
	    	        progress.show();
	        		String searchText = ((EditText) findViewById(R.id.editText1)).getText().toString();
	        		Log.i("searchTExt", searchText);
	        		Intent signup = new Intent(getApplicationContext(), SignupActivity.class);
	        		signup.putExtra(SEARCH_URL, searchText);
	        		Toast.makeText(getApplicationContext(), "connnecting to server...",
	        		Toast.LENGTH_SHORT).show();
	        		startActivity(signup);
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
//	public void onBackPressed() {
//	    // your code.
//		Intent back = new Intent(getApplicationContext(), FrontPage.class);
//		startActivity(back);
//	}
    
}
