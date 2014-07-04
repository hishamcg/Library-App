package com.example.myfirstapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SignInWaitingActivity extends Activity {
	private BroadcastReceiver mIntentReceiver;
	TextView timerTv;
	TextView mobNoVeryfyTv;
	private String pas_auth;

	private ProgressBar progressBar;
	static Boolean timeOut = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_waiting);
		//getting the 4 digit random number
		Intent auth_val = getIntent();
		pas_auth = auth_val.getStringExtra("pas_rand");
		
		mobNoVeryfyTv = (TextView) findViewById(R.id.SW_MobNoVeryfyDesctxt);

		timerTv = (TextView) findViewById(R.id.SW_TimeRemainigTv);
		progressBar = (ProgressBar) findViewById(R.id.SW_progressBar);
		// show 30 second time count down
		new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				timerTv.setText("Seconds Remaining : " + millisUntilFinished
						/ 1000);
			}
			//if 30 secs runout authentication fails.. and all data is reset to 0
			public void onFinish() {
				SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		        SharedPreferences.Editor   editor = preferences.edit();
			    //SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
			    editor.putString("AUTH_TOKEN", "0");
			    editor.putString("NUMBER", "0");
			    editor.putString("MEMBERSHIP_NO", "0");
			    editor.putString("DATE_OF_SIGNUP", "0");
			    System.out.println("im commiting");
			    editor.commit();
				timerTv.setText("Time Over");
				SignInWaitingActivity.this.finish();
			}
		}.start();

	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
		mIntentReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String msg = intent.getStringExtra("get_msg");

				// Process the sms format and extract body &amp; phoneNumber
				msg = msg.replace("\n", "");
				String body = msg.substring(msg.lastIndexOf(":") + 1,
						msg.length());
				String pNumber = msg.substring(0, msg.lastIndexOf(":"));

				// Add it to the list or do whatever you wish to
				Log.e("onResume", "" + msg + body + pNumber);

				// checking body content for verification code 

				if (body.equalsIgnoreCase("jb_"+pas_auth)) {

					Toast.makeText(getApplicationContext(),
							"Authentication Success.", 1).show();
					mobNoVeryfyTv.setText("Authentication Success.");
					Intent foo = new Intent(getApplicationContext(),FrontPage.class);
					startActivity(foo);

				} else {
					//random no does not match.. erasing all the data
					SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			        SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("AUTH_TOKEN", "0");
				    editor.putString("NUMBER", "0");
				    editor.putString("MEMBERSHIP_NO", "0");
				    editor.putString("DATE_OF_SIGNUP", "0");
				    System.out.println("im commiting");
				    editor.commit();
					mobNoVeryfyTv.setText("Authentication Fails.");
					SignInWaitingActivity.this.finish();

				}

			}
		};
		this.registerReceiver(mIntentReceiver, intentFilter);
	}

	@Override
	protected void onPause() {

		super.onPause();
		this.unregisterReceiver(this.mIntentReceiver);
	}

}

