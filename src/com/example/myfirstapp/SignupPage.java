package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SignupPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        progress = new ProgressDialog(this);
        final Button my_Button2 = (Button) findViewById(R.id.button2);
        final LinearLayout my_Button3 = (LinearLayout) findViewById(R.id.button3);
        final LinearLayout my_Button5 = (LinearLayout) findViewById(R.id.button5);

	    my_Button2.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		startActivity(searchlib);
        	}
        });
        my_Button3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
        		Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
        		startActivity(searchlib);
        	}
        });
        
        
        my_Button5.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		Intent searchlib = new Intent(getApplicationContext(), MainActivity.class);
        		searchlib.putExtra("check","not_logged_in");
        		searchlib.putExtra(SEARCH_URL,"zxyabc");
        		startActivity(searchlib);
        	}
        });
    }
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
    @Override
    public void onBackPressed()
    {
    	finish();
    }
    
}
