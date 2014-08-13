package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CallCustomerCare extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_customer_care);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final Button my_button = (Button) findViewById(R.id.my_button);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneN);
    
	    my_button.setOnClickListener(new Button.OnClickListener() {
	    	
			    public void onClick(View v){
			        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber.getText()));
			        call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        startActivity(call);}
			        });
	    

	    }
}

