package com.example.myfirstapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchPage extends Activity {
	private static final String SEARCH_URL = "url";
	private ProgressDialog progress;
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
        		
        		startActivity(signup);
        	}
        });
    }
//	@Override
//	public void onBackPressed() {
//	    // your code.
//	}
//    
}
