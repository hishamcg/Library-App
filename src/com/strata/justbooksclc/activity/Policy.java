package com.strata.justbooksclc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.strata.justbooksclc.R;

public class Policy extends Activity{
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
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
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
		
		setContentView(R.layout.policy);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		LinearLayout click0 = (LinearLayout) findViewById(R.id.click0);
		LinearLayout click1 = (LinearLayout) findViewById(R.id.click1);
		LinearLayout click2 = (LinearLayout) findViewById(R.id.click2);
		LinearLayout click3 = (LinearLayout) findViewById(R.id.click3);
		LinearLayout click4 = (LinearLayout) findViewById(R.id.click4);
		LinearLayout click5 = (LinearLayout) findViewById(R.id.click5);
		LinearLayout click6 = (LinearLayout) findViewById(R.id.click6);
		LinearLayout click7 = (LinearLayout) findViewById(R.id.click7);

		final LinearLayout policy0 = (LinearLayout) findViewById(R.id.policy0);
		final LinearLayout policy1 = (LinearLayout) findViewById(R.id.policy1);
		final LinearLayout policy2 = (LinearLayout) findViewById(R.id.policy2);
		final LinearLayout policy3 = (LinearLayout) findViewById(R.id.policy3);
		final LinearLayout policy4 = (LinearLayout) findViewById(R.id.policy4);
		final LinearLayout policy5 = (LinearLayout) findViewById(R.id.policy5);
		final LinearLayout policy6 = (LinearLayout) findViewById(R.id.policy6);
		final LinearLayout policy7 = (LinearLayout) findViewById(R.id.policy7);
		
		
		click0.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy0.getVisibility() == View.VISIBLE)
		      policy0.setVisibility(View.GONE);
		      else
		        policy0.setVisibility(View.VISIBLE); 
		  }
		});
		click1.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy1.getVisibility() == View.VISIBLE)
		      policy1.setVisibility(View.GONE);
		      else
		        policy1.setVisibility(View.VISIBLE); 
		  }
		});
		click2.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy2.getVisibility() == View.VISIBLE)
		      policy2.setVisibility(View.GONE);
		      else
		        policy2.setVisibility(View.VISIBLE); 
		  }
		});
		click3.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy3.getVisibility() == View.VISIBLE)
		      policy3.setVisibility(View.GONE);
		      else
		        policy3.setVisibility(View.VISIBLE); 
		  }
		});
		click4.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy4.getVisibility() == View.VISIBLE)
		      policy4.setVisibility(View.GONE);
		      else
		        policy4.setVisibility(View.VISIBLE); 
		  }
		});
		click5.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy5.getVisibility() == View.VISIBLE)
		      policy5.setVisibility(View.GONE);
		      else
		        policy5.setVisibility(View.VISIBLE); 
		  }
		});
		click6.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy6.getVisibility() == View.VISIBLE)
		      policy6.setVisibility(View.GONE);
		      else
		        policy6.setVisibility(View.VISIBLE); 
		  }
		});
		click7.setOnClickListener(new OnClickListener(){
		  public void onClick(View v){
		    if(policy7.getVisibility() == View.VISIBLE)
		      policy7.setVisibility(View.GONE);
		      else
		        policy7.setVisibility(View.VISIBLE); 
		  }
		});
	}
}
