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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

public class SingleMenuItemActivity  extends Activity {

	private ProgressDialog progress;
	Person person;
	//private static final String SERVER_BASE_URL = "192.168.2.133:4321";
	/*Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);}*/
	// JSON node keys
	private static final String USER_INFO = "info";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "title_id";
	private static final String RENTAL_ID = "rental_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String PICKUP_ORDER = "pickup_order_id";
	private static final String AVG_READING = "avg_reading_times";

	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        SearchManager searchManager =
              (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	               (SearchView) menu.findItem(R.id.action_search).getActionView();
	    searchView.setSearchableInfo(
	               searchManager.getSearchableInfo(getComponentName()));
	    //searchView.setOnQueryTextListener(this);
	    //return true;
	    int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	    TextView textView = (TextView) searchView.findViewById(id);
	    textView.setHintTextColor(0x88ffffff);
	    textView.setTextColor(0xffffffff);
	    return super.onCreateOptionsMenu(menu);
	  }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    if (itemId == R.id.action_search) {
	        /*Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
	        startActivity(searchlib);*/
	      return true;
	    }else if (itemId == android.R.id.home){
			finish();
			return true;
		} else {
	      return super.onOptionsItemSelected(item);
	      }
    }
	/*@Override
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
		} else if (itemId == android.R.id.home){
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	  }*/
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_view2);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(this);
        setWindowContentOverlayCompat();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String author = in.getStringExtra(TAG_AUTHOR);
        String category = in.getStringExtra(TAG_CATEGORY);
        String publisher = in.getStringExtra(TAG_LANGUAGE);
        String price = in.getStringExtra(TAG_PAGE);
        final String title = in.getStringExtra(TAG_TITLE);
        final String times_rented = in.getStringExtra(TIMES_RENTED);
        final String avg_reading = in.getStringExtra(AVG_READING);
        String image_url = in.getStringExtra(TAG_IMAGE_URL);
        final String pickup_order = in.getStringExtra(PICKUP_ORDER);
        String summary = in.getStringExtra(SUMMARY);
        final String rental_id = in.getStringExtra(RENTAL_ID);
        if (summary.equals("null")){
        	summary = "No summary available";
        }

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
        TextView lblTimesRented = (TextView) findViewById(R.id.times_rented);
        TextView lblAvgReading = (TextView) findViewById(R.id.avg_reading);
        TextView lblSummary = (TextView) findViewById(R.id.summary);
        ImageView lblimage = (ImageView) findViewById(R.id.image_label);
        final LinearLayout rental_btn = (LinearLayout) findViewById(R.id.button_rental);
        final LinearLayout remove = (LinearLayout) findViewById(R.id.remove);
        final LinearLayout add_to_list = (LinearLayout) findViewById(R.id.add_to_list);
        final LinearLayout pick_up = (LinearLayout) findViewById(R.id.pick_up);
        final RelativeLayout lblpickup_order = (RelativeLayout) findViewById(R.id.relativeLayout_pickup);
        final RelativeLayout action_view = (RelativeLayout) findViewById(R.id.relativeLayout_inside);
        final LinearLayout share = (LinearLayout) findViewById(R.id.share);
        if (pickup_order != null && pickup_order != "null"){
        	lblpickup_order.setVisibility(View.VISIBLE);
        	action_view.setVisibility(View.GONE);
        }

        final LinearLayout sign_in = (LinearLayout) findViewById(R.id.sign_in);
        final LinearLayout sign_up = (LinearLayout) findViewById(R.id.sign_up);
        final LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        final LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
        System.out.println("thi value = "+message);
        if (check_log.equals("logged_in")){
        	linearLayout3.setVisibility(View.GONE);
        	if (message.equals("create")){
        		remove.setVisibility(View.GONE);

	        }
        	else if(message.equals("current")){
        		pick_up.setVisibility(View.VISIBLE);
        		remove.setVisibility(View.GONE);
        		add_to_list.setVisibility(View.GONE);
        		rental_btn.setVisibility(View.GONE);
        	}
	        else{
	        	add_to_list.setVisibility(View.GONE);
	        }
        }else{
        	linearLayout2.setVisibility(View.GONE);
        }
        lblAuthor.setText(author);
        lblCategory.setText(category);
        lblPublisher.setText(publisher);
        lblPrice.setText(price);
        lblTitle.setText(title);
        lblimage.setImageBitmap(getBitmapFromURL(image_url));
        lblTimesRented.setText(times_rented);
        lblAvgReading.setText(avg_reading);
        lblSummary.setText(summary);


        rental_btn.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){


    	        AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want\n to rent this book");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/orders/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;

    	    		   InputStream inputStream = null;
    	        	   try {
    	        		    HttpParams httpParameters = new BasicHttpParams();
			        		// Set the timeout in milliseconds until a connection is established.
			        		// The default value is zero, that means the timeout is not used.
			        		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			        		// Set the default socket timeout (SO_TIMEOUT) 
			        		// in milliseconds which is the timeout for waiting for data.
			        		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
    	    	            // 1. create HttpClient
    	    	            HttpClient httpclient = new DefaultHttpClient();

    	    	            // 2. make POST request to the given URL
    	    	            HttpPost httpPost = new HttpPost(url);

    	    	            httpPost.setHeader("Content-type", "");

    	    	            // 8. Execute POST request to the given URL
    	    	            HttpResponse httpResponse = httpclient.execute(httpPost);

    	    	            // 9. receive response as inputStream
    	    	            inputStream = httpResponse.getEntity().getContent();

    	    	            // 10. convert inputstream to string
    	    	            if(inputStream != null)
    	    	            {
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
    	    	            	Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        } catch (Exception e) {
    	    	            Log.d("InputStream", e.getLocalizedMessage());
    	    	            Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.book);
    	        alert.show();


        	}
        });

        remove.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){
        		AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want to remove\n this book from wishlist");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/wishlists/destroy.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;

    	    		   InputStream inputStream = null;
    	        	   try {
    	        		    HttpParams httpParameters = new BasicHttpParams();
			        		// Set the timeout in milliseconds until a connection is established.
			        		// The default value is zero, that means the timeout is not used.
			        		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			        		// Set the default socket timeout (SO_TIMEOUT) 
			        		// in milliseconds which is the timeout for waiting for data.
			        		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
    	    	            // 1. create HttpClient
    	    	            HttpClient httpclient = new DefaultHttpClient();

    	    	            // 2. make POST request to the given URL
    	    	            HttpPost httpPost = new HttpPost(url);

    	    	            httpPost.setHeader("Content-type", "");

    	    	            // 8. Execute POST request to the given URL
    	    	            HttpResponse httpResponse = httpclient.execute(httpPost);

    	    	            // 9. receive response as inputStream
    	    	            inputStream = httpResponse.getEntity().getContent();

    	    	            // 10. convert inputstream to string
    	    	            if(inputStream != null)
    	    	            {
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
    	    	            	Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        } catch (Exception e) {
    	    	            Log.d("InputStream", e.getLocalizedMessage());
    	    	            Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.book);
    	        alert.show();

        	}
        });
        add_to_list.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){
        		AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want to add\n this book to wishlist");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/wishlists/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;

    	    		   InputStream inputStream = null;
    	        	   try {
    	        		    HttpParams httpParameters = new BasicHttpParams();
			        		// Set the timeout in milliseconds until a connection is established.
			        		// The default value is zero, that means the timeout is not used.
			        		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			        		// Set the default socket timeout (SO_TIMEOUT) 
			        		// in milliseconds which is the timeout for waiting for data.
			        		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
    	    	            // 1. create HttpClient
    	    	            HttpClient httpclient = new DefaultHttpClient();

    	    	            // 2. make POST request to the given URL
    	    	            HttpPost httpPost = new HttpPost(url);

    	    	            httpPost.setHeader("Content-type", "");

    	    	            // 8. Execute POST request to the given URL
    	    	            HttpResponse httpResponse = httpclient.execute(httpPost);

    	    	            // 9. receive response as inputStream
    	    	            inputStream = httpResponse.getEntity().getContent();

    	    	            // 10. convert inputstream to string
    	    	            if(inputStream != null)
    	    	            {
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
    	    	            	Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        } catch (Exception e) {
    	    	            Log.d("InputStream", e.getLocalizedMessage());
    	    	            Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.book);
    	        alert.show();

        	}
        });
        pick_up.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){
        		AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want\n to pickup this book");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/orders/pickup.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&rental_id="+rental_id+"&membership_no="+memb;

    	    		   InputStream inputStream = null;
    	        	   try {
    	        		    HttpParams httpParameters = new BasicHttpParams();
			        		// Set the timeout in milliseconds until a connection is established.
			        		// The default value is zero, that means the timeout is not used.
			        		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			        		// Set the default socket timeout (SO_TIMEOUT) 
			        		// in milliseconds which is the timeout for waiting for data.
			        		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
    	    	            // 1. create HttpClient
    	    	            HttpClient httpclient = new DefaultHttpClient();

    	    	            // 2. make POST request to the given URL
    	    	            HttpPost httpPost = new HttpPost(url);

    	    	            httpPost.setHeader("Content-type", "");

    	    	            // 8. Execute POST request to the given URL
    	    	            HttpResponse httpResponse = httpclient.execute(httpPost);

    	    	            // 9. receive response as inputStream
    	    	            inputStream = httpResponse.getEntity().getContent();

    	    	            // 10. convert inputstream to string
    	    	            if(inputStream != null)
    	    	            {
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
    	    	            	Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        } catch (Exception e) {
    	    	            Log.d("InputStream", e.getLocalizedMessage());
    	    	            Toast.makeText(getApplicationContext(),"Sorry something went wrong",Toast.LENGTH_LONG).show();
    	    	        }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.book);
    	        alert.show();
        	}
        });
        share.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent i=new Intent(android.content.Intent.ACTION_SEND);
        		i.setType("text/plain");
        		i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Check this Book ");
        		i.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.justbooksclc.com/titles/"+title_id);
        		startActivity(Intent.createChooser(i,"Share via"));
        	}
        });

        sign_in.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){

    	        Intent searchlib = new Intent(getApplicationContext(), MainPage.class);
        		startActivity(searchlib);
        	}
        });

        sign_up.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
    	        //Intent sign_up_call = new Intent(getApplicationContext(), CallCustomerCare.class);
        		Intent sign_up_call = new Intent(getApplicationContext(), HelpActivity.class);
        		startActivity(sign_up_call);
        	}
        });

    }
	//this was supposed to drop a shadow on actionbar
	private void setWindowContentOverlayCompat() {
	    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
	        // Get the content view
	        View contentView = findViewById(android.R.id.content);

	        // Make sure it's a valid instance of a FrameLayout
	        if (contentView instanceof FrameLayout) {
	            TypedValue tv = new TypedValue();

	            // Get the windowContentOverlay value of the current theme
	            if (getTheme().resolveAttribute(
	                    android.R.attr.windowContentOverlay, tv, true)) {

	                // If it's a valid resource, set it as the foreground drawable
	                // for the content view
	                if (tv.resourceId != 0) {
	                    ((FrameLayout) contentView).setForeground(
	                            getResources().getDrawable(tv.resourceId));
	                }
	            }
	        }
	    }
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