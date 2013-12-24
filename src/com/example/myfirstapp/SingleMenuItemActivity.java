package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	
	// JSON node keys
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_PUBLISHER = "publisher";
	private static final String TAG_PRICE = "price";
	private static final String TAG_TITLE = "title";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String author = in.getStringExtra(TAG_AUTHOR);
        String publisher = in.getStringExtra(TAG_PUBLISHER);
        String price = in.getStringExtra(TAG_PRICE);
        String title = in.getStringExtra(TAG_TITLE);
        
        // Displaying all values on the screen
        TextView lblAuthor = (TextView) findViewById(R.id.author_label);
        TextView lblPublisher = (TextView) findViewById(R.id.publisher_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        
        lblAuthor.setText(author);
        lblPublisher.setText(publisher);
        lblPrice.setText(price);
        lblTitle.setText(title);
    }
}