package com.strata.justbooksclc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePage extends Activity {
	
	ImageView mImage;
	public static String strSeparator = "__,__";
	
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
		String name = value.getString("USER_NAMES","NA");
			
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
		
		setContentView(R.layout.profile_page);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		TextView user_name = (TextView) findViewById(R.id.user_name);
		TextView user_email = (TextView) findViewById(R.id.user_email);
		TextView membership_no = (TextView) findViewById(R.id.membership_no);
		TextView user_branch = (TextView) findViewById(R.id.user_branch);
		TextView user_reading_score = (TextView) findViewById(R.id.user_reading_score);
		TextView user_plan = (TextView) findViewById(R.id.user_plan);
		TextView user_full_name = (TextView) findViewById(R.id.user_full_name);
		TextView user_address = (TextView) findViewById(R.id.user_address);
		TextView user_location = (TextView) findViewById(R.id.user_location);
		TextView user_city = (TextView) findViewById(R.id.user_city);
		TextView user_state = (TextView) findViewById(R.id.user_state);
		TextView user_pincode = (TextView) findViewById(R.id.user_pincode);
		TextView user_phone = (TextView) findViewById(R.id.user_phone);
		
		String[] arr_name = name.split(strSeparator);
		for (int i=0;i<3;i++){
			if (arr_name[i].equals("null"))arr_name[i] = "";
		}
		user_name.setText(arr_name[0]);
		user_full_name.setText(arr_name[0]+" "+arr_name[1]+" "+arr_name[1]);
		user_email.setText(value.getString("EMAIL", "NA"));
		membership_no.setText(value.getString("MEMBERSHIP_NO", "NA"));
		user_branch.setText(value.getString("BRANCH", "NA"));
		user_reading_score.setText(value.getString("READING_SCORE", "0"));
		user_plan.setText(value.getString("PLAN", "NA"));
		user_address.setText(value.getString("ADDRESS", "NA"));
		user_location.setText(value.getString("LOCALITY", "NA"));
		user_city.setText(value.getString("CITY", "NA"));
		user_state.setText(value.getString("STATE", "NA"));
		user_pincode.setText(value.getString("PINCODE", "NA"));
		user_phone.setText(value.getString("NUMBER", "NA"));
//		mImage = (ImageView)findViewById(R.id.imageView1);
//		loadDataFromAsset();
	}
	
  	public static String[] convertStringToArray(String str){
  	    String[] arr = str.split(strSeparator);
  	    return arr;
  	}
	
//	public void loadDataFromAsset() {
//        // load image
//        try {
//            // get input stream
//            InputStream ims = getAssets().open("mine.jpg");
//            // load image as Drawable
//            Drawable d = Drawable.createFromStream(ims, null);
//            // set image to ImageView
//            mImage.setImageDrawable(d);
//        }
//        catch(IOException ex) {
//            return;
//        }
// 
//    }
}
