package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    if (itemId == android.R.id.home){
	    	finish();
	    	return true;
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
	  }
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final Button btn_care = (Button) findViewById(R.id.btn_care);
		btn_care.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				 String phoneNumber = "9740935455";
			     Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
			     call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			     startActivity(call);
			}
		});
	}

}
