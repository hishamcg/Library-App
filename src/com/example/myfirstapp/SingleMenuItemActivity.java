package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

public class SingleMenuItemActivity  extends Activity {
	
	Person person;	
	private ProgressDialog progress;
	//private static final String SERVER_BASE_URL = "192.168.2.133:4321";
	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);}
	// JSON node keys
	private static final String USER_INFO = "info";
	private static final String TAG_AUTHOR = "author_id";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "title_id";
	private static final String RENTAL_ID = "rental_id";
	String rental_id = "";
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.activity_main_actions, menu);
      //return true;
      return super.onCreateOptionsMenu(menu);
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		progress = new ProgressDialog(this);
		progress.setMessage("Loading");
	    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progress.setIndeterminate(true);
	    progress.show();
		int itemId = item.getItemId();
		if (itemId == R.id.action_back) {
			// Single menu item is selected do something
          // Ex: launching new activity/screen or show alert message
          finish();
			return true;
		} else if (itemId == R.id.action_search) {
			Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
  		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_storage) {
			Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
  		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_place) {
			Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
  		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_help) {
			Intent about = new Intent(getApplicationContext(), HelpActivity.class);
  		startActivity(about);
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	  }
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_view2);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
        progress = new ProgressDialog(this);
        progress.hide();
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String author = in.getStringExtra(TAG_AUTHOR);
        String category = in.getStringExtra(TAG_CATEGORY);
        String publisher = in.getStringExtra(TAG_LANGUAGE);
        String price = in.getStringExtra(TAG_PAGE);
        String title = in.getStringExtra(TAG_TITLE);
        String image = in.getStringExtra(TAG_IMAGE_URL);
        String message = in.getStringExtra("message");
        String check_log = in.getStringExtra("check");
        final String title_id = in.getStringExtra(TAG_ID);
        
        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		final String auth_token = value.getString("AUTH_TOKEN","");
		final String memb = value.getString("MEMBERSHIP_NO","");
		final String numb = value.getString("NUMBER","");
        
        // Displaying all values on the screen
        TextView lblAuthor = (TextView) findViewById(R.id.author_label);
        TextView lblCategory = (TextView) findViewById(R.id.category_label);
        TextView lblPublisher = (TextView) findViewById(R.id.publisher_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        ImageView lblimage = (ImageView) findViewById(R.id.image_label);
        final LinearLayout rental_btn = (LinearLayout) findViewById(R.id.button_rental);
        final LinearLayout click2 = (LinearLayout) findViewById(R.id.click2);
        final LinearLayout click3 = (LinearLayout) findViewById(R.id.click3);
        final LinearLayout click4 = (LinearLayout) findViewById(R.id.click4);
        click4.setVisibility(View.GONE);
        
        final LinearLayout sign_in = (LinearLayout) findViewById(R.id.sign_in);
        final LinearLayout sign_up = (LinearLayout) findViewById(R.id.sign_up);
        final LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        final LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        System.out.println("thi value = "+message);
        if (check_log.equals("logged_in")){
        	linearLayout3.setVisibility(View.GONE);
        	if (message.equals("create")){
	        	click2.setVisibility(View.GONE);
	        	
	        }
        	else if(message.equals("current")){
        		rental_id = in.getStringExtra(RENTAL_ID);
        		click4.setVisibility(View.VISIBLE);
        		click2.setVisibility(View.GONE);
        		click3.setVisibility(View.GONE);
        		rental_btn.setVisibility(View.GONE);
        	}
	        else{
	        	click3.setVisibility(View.GONE);
	        }
        }else{
        	linearLayout2.setVisibility(View.GONE);
        }
        lblAuthor.setText(author);
        lblCategory.setText(category);
        lblPublisher.setText(publisher);
        lblPrice.setText(price);
        lblTitle.setText(title);
        lblimage.setImageBitmap(getBitmapFromURL(image));
        
        
        rental_btn.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/orders/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;
    			
    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);
    	 
//    	            String json = "";
//    	 
//    	            // 3. build jsonObject
//    	            JSONObject jsonObject = new JSONObject();
//    	            jsonObject.accumulate("api_key", auth_token);
//    	            jsonObject.accumulate("phone", numb);
//    	            jsonObject.accumulate("membership_no", memb);
//    	            jsonObject.accumulate("title_id", title_id);
//    	 
//    	            // 4. convert JSONObject to JSON to String
//    	            json = jsonObject.toString();
//    	 
//    	            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
//    	            // ObjectMapper mapper = new ObjectMapper();
//    	            // json = mapper.writeValueAsString(person); 
//    	 
//    	            // 5. set json to StringEntity
//    	            StringEntity se = new StringEntity(json);
    	 
    	            // 6. set httpPost Entity
    	            //httpPost.setEntity(se);
    	 
    	            // 7. Set some headers to inform server about the type of the content   
    	            //httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();
    	            
    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString(USER_INFO);
	    	            Toast.makeText(getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
    	        progress.hide();
        	}
        });
        
        click2.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/wishlists/destroy.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;

    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);

    	            // 6. httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();

    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString(USER_INFO);
	    	            Toast.makeText(getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
    	        progress.hide();
        	}
        });
        click3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/wishlists/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;
    			
    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);

    	            // 6. httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();

    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString(USER_INFO);
	    	            Toast.makeText(getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
    	        progress.hide();
        	}
        });
        click4.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/orders/pickup.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&rental_id="+rental_id+"&membership_no="+memb;
    			
    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);

    	            // 6. httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();

    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString(USER_INFO);
	    	            Toast.makeText(getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
    	        progress.hide();
        	}
        });
        
        sign_in.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        
    	        Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		startActivity(searchlib);
        	}
        });
        
        sign_up.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
        		progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
    	        
    	        Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		startActivity(searchlib);
        	}
        });
       
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
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
}