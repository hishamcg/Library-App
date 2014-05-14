package com.example.myfirstapp;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	
	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);}
	// JSON node keys
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_PUBLISHER = "publisher";
	private static final String TAG_PRICE = "price";
	private static final String TAG_TITLE = "title";
	private static final String TAG_IMAGE_URL = "image";
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
        String image = in.getStringExtra(TAG_IMAGE_URL);
        
        // Displaying all values on the screen
        TextView lblAuthor = (TextView) findViewById(R.id.author_label);
        TextView lblPublisher = (TextView) findViewById(R.id.publisher_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        ImageView lblimage = (ImageView) findViewById(R.id.image_label);
        
        lblAuthor.setText(author);
        lblPublisher.setText(publisher);
        lblPrice.setText(price);
        lblTitle.setText(title);
        /*try {
			lblimage.setBackgroundDrawable(drawable_from_url(image,"@drawable/ic_launcher"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}