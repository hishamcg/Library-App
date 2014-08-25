package com.strata.justbooksclc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.strata.justbooksclc.R;

public class SignInWaitingActivity extends Activity {
	private BroadcastReceiver mIntentReceiver;
	TextView timerTv;
	TextView mobNoVeryfyTv;
	private String pas_auth;
	String auth_token;
	String phone_no;
	String membership_no;
	String date_of_signup;
	static Boolean timeOut = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_waiting);
		//getting the 4 digit random number
		Intent auth_val = getIntent();
		pas_auth = auth_val.getStringExtra("pas_rand");
		auth_token = auth_val.getStringExtra("AUTH_TOKEN");
		phone_no = auth_val.getStringExtra("NUMBER");
		membership_no = auth_val.getStringExtra("MEMBERSHIP_NO");
		date_of_signup = auth_val.getStringExtra("DATE_OF_SIGNUP");
		
		mobNoVeryfyTv = (TextView) findViewById(R.id.SW_MobNoVeryfyDesctxt);

		timerTv = (TextView) findViewById(R.id.SW_TimeRemainigTv);
		// show 30 second time count down
		new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				timerTv.setText("Seconds Remaining : " + millisUntilFinished
						/ 1000);
			}
			public void onFinish() {
				Toast.makeText(getApplicationContext(),
						"Authentication Failed. Please make sure that you have DND de-activated and try again.", Toast.LENGTH_LONG).show();
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
				String body = msg.substring(msg.indexOf(":") + 1,
						msg.length());
				String pNumber = msg.substring(0, msg.indexOf(":"));

				// Add it to the list or do whatever you wish to
				Log.e("onResume", "" + msg + body + pNumber);

				// checking body content for verification code 

				if (body.equalsIgnoreCase("Dear Member, your access token to Justbooksclc app is: "+pas_auth)) {

					Toast.makeText(getApplicationContext(),
							"Authentication Success.", Toast.LENGTH_SHORT).show();
					mobNoVeryfyTv.setText("Authentication Success.");
					SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
			        SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("AUTH_TOKEN", auth_token);
				    editor.putString("NUMBER", phone_no);
				    editor.putString("MEMBERSHIP_NO", membership_no);
				    editor.putString("DATE_OF_SIGNUP", date_of_signup);
				    System.out.println("im commiting");
				    editor.commit();
					Intent foo = new Intent(getApplicationContext(),PageZero.class);
					startActivity(foo);
					finish();
				} else {
					/*Toast.makeText(getApplicationContext(),
							"Authentication Failed. Please make sure that you have DND de-activated and try again.", Toast.LENGTH_LONG).show();
					timerTv.setText("Time Over");
					finish();*/
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

