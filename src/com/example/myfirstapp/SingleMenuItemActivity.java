package com.example.myfirstapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	
	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);}
	// JSON node keys
	private static final String TAG_AUTHOR = "author_id";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String author = in.getStringExtra(TAG_AUTHOR);
        String category = in.getStringExtra(TAG_CATEGORY);
        String publisher = in.getStringExtra(TAG_LANGUAGE);
        String price = in.getStringExtra(TAG_PAGE);
        String title = in.getStringExtra(TAG_TITLE);
        String image = in.getStringExtra(TAG_IMAGE_URL);
        
        
        
        // Displaying all values on the screen
        TextView lblAuthor = (TextView) findViewById(R.id.author_label);
        TextView lblCategory = (TextView) findViewById(R.id.category_label);
        TextView lblPublisher = (TextView) findViewById(R.id.publisher_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        ImageView lblimage = (ImageView) findViewById(R.id.image_label);
        
        lblAuthor.setText(author);
        lblCategory.setText(category);
        lblPublisher.setText(publisher);
        lblPrice.setText(price);
        lblTitle.setText(title);
        lblimage.setImageBitmap(getBitmapFromURL(image));
        
      /*  try {
			lblimage.setImageDrawable(drawable_from_url(image,""));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        Log.e("src",src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        Log.e("Bitmap","returned");
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
}

}