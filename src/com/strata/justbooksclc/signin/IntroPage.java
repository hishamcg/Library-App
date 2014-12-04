package com.strata.justbooksclc.signin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.R;

public class IntroPage extends Activity {
	
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
		//settingup new relic
		NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String my_theme = value.getString("MY_THEME", "");
			
		if (my_theme.equals("green"))
			setTheme(R.style.MyThemeGreen);
		else if (my_theme.equals("brown"))
			setTheme(R.style.MyThemeBrown);
		else if (my_theme.equals("violet"))
			setTheme(R.style.MyThemeViolet);
		else if (my_theme.equals("blue"))
			setTheme(R.style.MyThemeBlue);
		else
			setTheme(R.style.MyTheme);
		
		setContentView(R.layout.intro_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final Button btn_signin = (Button) findViewById(R.id.btn_signin);
		btn_signin.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent in = new Intent(getApplicationContext(),SigninActivity.class);
				startActivity(in);
				finish();
			}
		});
		
		final Button btn_signup = (Button) findViewById(R.id.btn_signup);
		btn_signup.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent in = new Intent(getApplicationContext(),Signup.class);
				startActivity(in);
				finish();
			}
		});
		final Button btn_skip = (Button) findViewById(R.id.btn_skip);
		btn_skip.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				finish();
			}
		});
		
	}

}
