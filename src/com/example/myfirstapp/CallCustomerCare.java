package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CallCustomerCare extends Activity {
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
        setContentView(R.layout.call_customer_care);
        final Button my_button = (Button) findViewById(R.id.my_button);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneN);
    
	    my_button.setOnClickListener(new Button.OnClickListener() {
	    	
			    public void onClick(View v){
			        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber.getText()));
			        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(call);}
			        });
	    

	    }
}

