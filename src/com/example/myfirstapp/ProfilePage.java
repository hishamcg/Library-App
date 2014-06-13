package com.example.myfirstapp;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ProfilePage extends Activity {
	
	ImageView mImage;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_page);
		
		mImage = (ImageView)findViewById(R.id.imageView1);
		loadDataFromAsset();
	}
	public void loadDataFromAsset() {
        // load image
        try {
            // get input stream
            InputStream ims = getAssets().open("mine.jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            mImage.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
 
    }
}
