package com.strata.justbooksclc.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.strata.justbooksclc.R;

public class ExpiredPage extends Activity {
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expired_page);
		
		final Button btn_care = (Button) findViewById(R.id.btn_care);
		btn_care.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				 String phoneNumber = "18001022665";
			     Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
			     call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			     startActivity(call);
			}
		});
	}

}
