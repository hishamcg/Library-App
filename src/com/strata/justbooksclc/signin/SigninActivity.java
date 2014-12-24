package com.strata.justbooksclc.signin;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;

public class SigninActivity extends Activity {
	private ProgressDialog progress;
	// JSON Node names
	private static final String SUCCESS = "success";
	String NUMBER;
	private static final String USER_INFO = "info";
	private static final String AUTH_TOKEN = "api_key";
	private static final String MEMBERSHIP_NO = "membership_no";
	private static final String PLAN_NAME = "plan_name";
	private static final String EXPIRY_DATE = "expiry_date";
	private String SUCC = "false";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        progress = new ProgressDialog(this);

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        final Button my_Button = (Button) findViewById(R.id.button1);
        final TextView no_connection = (TextView) findViewById(R.id.no_connection);
        final TextView hint_signin = (TextView) findViewById(R.id.hint_signin);
        final EditText my_numb = (EditText) findViewById(R.id.editText1);
        if (number != null){
        	my_numb.setText(CorrectPhoneFormat(number));
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

        if (isNetworkAvailable()){
	        my_Button.setOnClickListener(new Button.OnClickListener() {
	        	@SuppressLint("InlinedApi")
				public void onClick(View v){
	        		final String phone_no = CorrectPhoneFormat(my_numb.getText().toString());
	        		Log.i("phone_no", phone_no);
	        		Toast.makeText(getApplicationContext(), "connnecting to server...",
	    	        Toast.LENGTH_SHORT).show();


	        		String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/sessions.json?phone=" + phone_no;
	        		// Creating JSON Parser instance
	        		JSONParser jParser = new JSONParser();
	      			JSONObject json = jParser.getJSONFromUrl(url);
	        		if (json != null){
		        		try { // getting JSON string from URL
		      			  SUCC = json.getString(SUCCESS);
		      			  System.out.println(SUCC);
		      			  if (SUCC.equals("true") ){
		      				  String INFO = json.getString(USER_INFO);
		      				  System.out.println(INFO);
		      				  JSONArray DATA = json.getJSONArray("data");
		      				  int d_length = DATA.length();
		      				  final String[][] data_list = new String[d_length][4];
		      				  String[] show_list = new String[d_length];
		      				  for (int i = 0; i < d_length; i++){
		      					JSONObject c = DATA.getJSONObject(i);
		      					data_list[i][0] = c.getString(AUTH_TOKEN);
		      					data_list[i][1] = c.getString(MEMBERSHIP_NO);
		      					data_list[i][2] = c.getString(PLAN_NAME);
		      					data_list[i][3] = c.getString(EXPIRY_DATE);
		      					show_list[i] = "\nMember No: "+data_list[i][1]+"\nPlan Name: "+data_list[i][2]+"\nExpiry Date: "+data_list[i][3]+"\n";
		      				  }
		      				  ContextThemeWrapper cw = new ContextThemeWrapper( SigninActivity.this, R.style.AlertDialogTheme );
		      				  AlertDialog.Builder builder = new AlertDialog.Builder(cw);
		      				  builder.setTitle("Select the account that you want to login")
		      			           .setItems(show_list, new DialogInterface.OnClickListener() {
		      			              public void onClick(DialogInterface dialog, int which) {
		      			            	  Random r = new Random();
		      			            	  final int pas = r.nextInt(10000 - 1000) + 1000;
		      			            	  //System.out.println(pas);
		      			            	  //comment to bypass authentication via sms
		      			            	  String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/send_otp.json?phone=" + phone_no+"&otp="+String.valueOf(pas);
		      			            	  System.out.println("score here"+ phone_no);
		      			            	  // Creating JSON Parser instance
		      			            	  JSONParser jParser = new JSONParser();
		      			      			  jParser.getJSONFromUrl(url);
			   		      				  
			   		      				  //Intent checking_auth = new Intent(getApplicationContext(), PageZero.class);
			   			      			  Intent checking_auth = new Intent(getApplicationContext(), SignInWaitingActivity.class);
			   		        	          checking_auth.putExtra("pas_rand", String.valueOf(pas));
			   		        	          checking_auth.putExtra("AUTH_TOKEN", data_list[which][0]);
			   		        	          checking_auth.putExtra("NUMBER", phone_no);
			   		        	          checking_auth.putExtra("MEMBERSHIP_NO", data_list[which][1]);
			   			        	      startActivity(checking_auth);
			   			        	      finish();
		      			           }
		      			    });
		      				builder.setIcon(R.drawable.gcm_icon);
		      				builder.show();
		      			  }else{
		      				  Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_SHORT);
		      				  toast.setGravity(Gravity.TOP, 0, 170);
		      				  toast.show();
		      				  finish();
		      			  }


			      		} catch (JSONException e) {
			      			e.printStackTrace();
			      			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_SHORT);
		      				toast.setGravity(Gravity.TOP, 0, 170);
		      				toast.show();
		      				finish();
			      		}
	        		}else{
	        			Toast toast = Toast.makeText(getApplicationContext(),"      Login Failed \nPlease Try Again Later",Toast.LENGTH_SHORT);
	      				toast.setGravity(Gravity.TOP, 0, 170);
	      				toast.show();
	      				finish();
	        		}
	        	}
	        });
        }
        else {
        	hint_signin.setVisibility(View.GONE);
        	no_connection.setVisibility(View.VISIBLE);
        	Toast.makeText(getApplicationContext(), "NO internet Connection!",
		    Toast.LENGTH_SHORT).show();
        }
    }
	
	public String CorrectPhoneFormat(String number){
		if (number.length()>10){
			number = number.substring(number.length()-10);
		}
		return number;
	}
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public void onDestroy() {
	    super.onDestroy();
	}

}
