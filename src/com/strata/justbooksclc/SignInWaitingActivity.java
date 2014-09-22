package com.strata.justbooksclc;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SignInWaitingActivity extends Activity {
	private BroadcastReceiver mIntentReceiver;
	TextView timerTv;
	TextView mobNoVeryfyTv;
	ProgressBar progress;
	LinearLayout linear;
	Button otpButton;
	Button btn_skip;
	EditText otp_input;
	private String pas_rand;
	String auth_token;
	String phone_no;
	String membership_no;
	static Boolean timeOut = true;
	
	CountDownTimer count_down;
	CountDownTimer count_down_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_waiting);
		//getting the 4 digit random number
		Intent auth_val = getIntent();
		pas_rand = auth_val.getStringExtra("pas_rand");
		auth_token = auth_val.getStringExtra("AUTH_TOKEN");
		phone_no = auth_val.getStringExtra("NUMBER");
		membership_no = auth_val.getStringExtra("MEMBERSHIP_NO");
		
		int waiting_time = 60;
		
		mobNoVeryfyTv = (TextView) findViewById(R.id.SW_MobNoVeryfyDesctxt);
		timerTv = (TextView) findViewById(R.id.SW_TimeRemainigTv);
		progress = (ProgressBar) findViewById(R.id.SW_progressBar);
		linear = (LinearLayout) findViewById(R.id.type_lay);
		otpButton = (Button) findViewById(R.id.btn_otp);
		otp_input = (EditText) findViewById(R.id.SW_OtpText);
		btn_skip = (Button) findViewById(R.id.btn_skip);
		
		// show 30 second time count down
		count_down = new CountDownTimer(waiting_time*1000, 1000) {

			public void onTick(long millisUntilFinished) {
				timerTv.setText("Seconds Remaining : " + millisUntilFinished
						/ 1000);
			}

			@Override
			public void onFinish() {
				ManualTypeOtp();
			}
			
		}.start();
		btn_skip.setOnClickListener(new Button.OnClickListener() {
        	@SuppressLint("InlinedApi")
			public void onClick(View v){
        		count_down.cancel();
        		ManualTypeOtp();
        	}
        });
		
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

				if (body.equalsIgnoreCase("Dear Member, your access token to Justbooksclc app is: "+pas_rand)) {
					AuthenticationComplete();
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
	public void ManualTypeOtp(){
		count_down.cancel();
		if (timeOut){
			Toast.makeText(getApplicationContext(),"Not able to retrieve your otp sms", Toast.LENGTH_LONG).show();
		}
		progress.setVisibility(View.GONE);
		linear.setVisibility(View.VISIBLE);
		btn_skip.setVisibility(View.GONE);
		timerTv.setVisibility(View.GONE);
		mobNoVeryfyTv.setText("Sorry! was not able to retrieve sms verification key. Please enter it manually");
		//SignInWaitingActivity.this.finish();
		count_down_type = new CountDownTimer(300*1000, 1000) {
			public void onTick(long millisUntilFinished) {}
	
			@Override
			public void onFinish() {
				if (timeOut){
				Toast.makeText(getApplicationContext(),
						"Authentication Timed Out. Please Try Again Later.", Toast.LENGTH_LONG).show();}
				SignInWaitingActivity.this.finish();
			}
		}.start();
		otpButton.setOnClickListener(new Button.OnClickListener() {
        	@SuppressLint("InlinedApi")
			public void onClick(View v){
        		String otp_code =  otp_input.getText().toString();
        		if (!otp_code.isEmpty() && otp_code != null){
        			if (otp_code.equals(pas_rand)){
        				AuthenticationComplete();
        			}else{
        				if (timeOut){
    						Toast.makeText(getApplicationContext(),
    								"Authentication Failed Please Try Again Later.", Toast.LENGTH_LONG).show();}
        				SignInWaitingActivity.this.finish();
        			}
        		}
        		
        	}
        });
	}
	public void AuthenticationComplete(){
		timeOut = false;
		count_down.cancel();
		
		long datevalue = new Date().getTime();
			String data_of_signup = String.valueOf(datevalue);

		SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor   editor = preferences.edit();
	    editor.putString("AUTH_TOKEN", auth_token);
	    editor.putString("NUMBER", phone_no);
	    editor.putString("MEMBERSHIP_NO", membership_no);
	    editor.putString("DATE_OF_SIGNUP", data_of_signup);
	    System.out.println("im commiting");
	    editor.commit();
		Intent foo = new Intent(getApplicationContext(),PageZero.class);
		foo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(foo);
		Toast.makeText(getApplicationContext(),"Authentication Successful", Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	protected void onPause() {

		super.onPause();
		this.unregisterReceiver(this.mIntentReceiver);
	}
	 public void onBackPressed(){
		 super.onBackPressed();
		 count_down.cancel();
		 count_down_type.cancel();
	 }

}

