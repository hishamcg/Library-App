package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_activity);
		
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
