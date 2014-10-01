package com.strata.justbooksclc;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class BookBand extends Activity{
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
		setContentView(R.layout.book_band);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
