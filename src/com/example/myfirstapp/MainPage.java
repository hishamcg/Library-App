package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        progress = new ProgressDialog(this);
        progress.hide();
        final Button my_Button = (Button) findViewById(R.id.button1);
        final Button no_connection = (Button) findViewById(R.id.no_connection);
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
	        		Toast.makeText(getApplicationContext(), "nothing here!",
	        		Toast.LENGTH_LONG).show();
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
	public void onRestart(){
		super.onResume();
		progress.hide();
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

//	public void onBackPressed() {
//	    // your code.
//		Intent back = new Intent(getApplicationContext(), FrontPage.class);
//		startActivity(back);
//	}
    
}
