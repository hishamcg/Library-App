package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.front_page, menu);
      menuInflater.inflate(R.menu.search_page_menu, menu);
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
		} else if (itemId == R.id.action_storage) {
			Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
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
        setContentView(R.layout.search_page);
        progress = new ProgressDialog(this);
        progress.hide();
        final Button my_Button = (Button) findViewById(R.id.button1);
//        final EditText my_Text = (EditText) findViewById(R.id.editText1);
//        final String fin = my_Text.getText().toString();
        
        my_Button.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading...");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		String searchText = ((EditText) findViewById(R.id.editText1)).getText().toString();
        		Log.i("searchTExt", searchText);
        		Intent signup = new Intent(getApplicationContext(), MainActivity.class);
        		signup.putExtra(SEARCH_URL, searchText);
				signup.putExtra("check", "logged_in");
        		
        		startActivity(signup);
        	}
        });
    }
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
}
