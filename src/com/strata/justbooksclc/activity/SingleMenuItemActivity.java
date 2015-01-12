package com.strata.justbooksclc.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;
import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.ShakeEventListener;
import com.strata.justbooksclc.SharedValue;
import com.strata.justbooksclc.signin.SigninActivity;
import com.strata.justbooksclc.signin.Signup;

public class SingleMenuItemActivity  extends Activity {

	Person person;
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
	private static final String DeliveryOrderId = "delivery_order_id";
	private static final String ISBN = "isbn";

	private static final String AllowCancel = "allow_cancel";
	private static final String OrderType = "order_type";
	private static final String Status = "status";

	private JSONParse json_parse = new JSONParse();
	private JSONParseSummary json_summary;
	private JSONRequest json_track;
	String branch_name;
	String title_id;
	long time_of_last_shake = 0;
	long initial_time = System.currentTimeMillis();
	long buffer_time_for_shake = 1000;
	private String order_type = null;
	private SensorManager mSensorManager;
	private ShakeEventListener mSensorListener;
	private TextView lblSummary;

	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_page_menu, menu);
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
	      return true;
	    }else if (itemId == android.R.id.home){
			finish();
			return true;
		} else {
	      return super.onOptionsItemSelected(item);
	      }
    }

	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		final String auth_token = value.getString("AUTH_TOKEN","");
		final String memb = value.getString("MEMBERSHIP_NO","");
		final String numb = value.getString("NUMBER","");
		final String login_status = value.getString("LOGIN_STATUS","");
		branch_name = value.getString("BRANCH","");
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

        setContentView(R.layout.single_list_detail_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        String image_url = in.getStringExtra(TAG_IMAGE_URL);
        //String summary = in.getStringExtra(SUMMARY);
        final String title = in.getStringExtra(TAG_TITLE);
        final String times_rented = in.getStringExtra(TIMES_RENTED);
        final String avg_reading = in.getStringExtra(AVG_READING);
        final String pickup_order = in.getStringExtra(PICKUP_ORDER);
        final String rental_id = in.getStringExtra(RENTAL_ID);
        final String isbn = in.getStringExtra(ISBN);
        final String delivery_order_id = in.getExtras().getString(DeliveryOrderId,null);
        order_type = in.getExtras().getString(OrderType,null);

        String message = in.getStringExtra("message");
        title_id = in.getStringExtra(TAG_ID);
        // Displaying all values on the screen
        TextView lblAuthor = (TextView) findViewById(R.id.author_label);
        TextView lblCategory = (TextView) findViewById(R.id.category_label);
        TextView lblPublisher = (TextView) findViewById(R.id.publisher_label);
        TextView lblPrice = (TextView) findViewById(R.id.price_label);
        TextView lblTitle = (TextView) findViewById(R.id.title_label);
        TextView lblTimesRented = (TextView) findViewById(R.id.times_rented);
        TextView lblAvgReading = (TextView) findViewById(R.id.avg_reading);
        lblSummary = (TextView) findViewById(R.id.summary);
        //final TextView lblpickup_order = (TextView) findViewById(R.id.pickup_order);
        ImageView lblimage = (ImageView) findViewById(R.id.image_label);

        final RelativeLayout avail_layout = (RelativeLayout) findViewById(R.id.avail_layout);

        final LinearLayout rental_btn = (LinearLayout) findViewById(R.id.button_rental);
        final LinearLayout remove = (LinearLayout) findViewById(R.id.remove);
        final LinearLayout add_to_list = (LinearLayout) findViewById(R.id.add_to_list);
        final LinearLayout pick_up = (LinearLayout) findViewById(R.id.pick_up);
        final LinearLayout order_cancel = (LinearLayout) findViewById(R.id.order_cancel);
        final LinearLayout track = (LinearLayout) findViewById(R.id.track);
        //final RelativeLayout pickup_layout = (RelativeLayout) findViewById(R.id.relativeLayout_pickup);
        final LinearLayout share = (LinearLayout) findViewById(R.id.share);
        /*if (pickup_order != null && !pickup_order.equals("")){
        	lblpickup_order.setText(pickup_order);
        	pickup_layout.setVisibility(View.VISIBLE);
        }*/
        final LinearLayout sign_in = (LinearLayout) findViewById(R.id.sign_in);
        final LinearLayout sign_up = (LinearLayout) findViewById(R.id.sign_up);
        //fecth summary from google with isbn
        if (!isbn.isEmpty() && !isbn.equals("NOISBN") && !isbn.equals("null")){
        	//summary = "No summary available";
        	String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn;
        	json_summary = new JSONParseSummary();
        	json_summary.execute(url);
        }else{
        	lblSummary.setText("summary not available");
        }

        if (login_status.equals("user") || login_status.equals("exp_user")){
        	sign_in.setVisibility(View.GONE);
        	sign_up.setVisibility(View.GONE);
        	if (message.equals("create")){
        		remove.setVisibility(View.GONE);
        		json_parse.execute();
	        }
        	else if(message.equals("current")){
        		if(pickup_order.isEmpty() || pickup_order.equals("null")){
        			pick_up.setVisibility(View.VISIBLE);
        		}
        		remove.setVisibility(View.GONE);
        		add_to_list.setVisibility(View.GONE);
        		rental_btn.setVisibility(View.GONE);
        		avail_layout.setVisibility(View.GONE);
        	}else if (message.equals("pending")){
        		//lblpickup_order.setText("rental order in process...");
        		if(in.getBooleanExtra(AllowCancel,false)){
        			order_cancel.setVisibility(View.VISIBLE);
        		}
        		if(order_type != null && order_type.equals("D")){
        			track.setVisibility(View.VISIBLE);
        		}
        		pick_up.setVisibility(View.GONE);
        		remove.setVisibility(View.GONE);
        		add_to_list.setVisibility(View.GONE);
        		rental_btn.setVisibility(View.GONE);
        		json_parse.execute();
        	}else{
	        	add_to_list.setVisibility(View.GONE);
        		json_parse.execute();
	        }
        }else{
        	rental_btn.setVisibility(View.GONE);
        	pick_up.setVisibility(View.GONE);
        	remove.setVisibility(View.GONE);
        	add_to_list.setVisibility(View.GONE);
        	avail_layout.setVisibility(View.GONE);
        }
        lblAuthor.setText(author);
        lblCategory.setText(category);
        lblPublisher.setText("Language : "+publisher);
        lblPrice.setText("Pages : "+price);
        lblTitle.setText(title);
        //lblimage.setImageBitmap(getBitmapFromURL(image_url));
        lblTimesRented.setText(times_rented);
        //lblSummary.setText(summary);
        lblAvgReading.setText(avg_reading);
        
        //------setting image-------
  		Picasso.with(this)
  		.load(image_url)
  		.error(R.drawable.ic_launcher)
  		.into(lblimage);
  		//--------------------------
        
        if (avg_reading.equals("NA")){
        	TextView lbldays_plain_text = (TextView) findViewById(R.id.days_plain_text);
        	lbldays_plain_text.setVisibility(View.GONE);
        }
        
        rental_btn.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){


    	        AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want\n to rent this book");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/orders/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;
    	        	   String success = httpToPostRequest(url);
    	        	   if(success.equals("true")){
    	        		   rental_btn.setVisibility(View.GONE);
    	        		   SharedValue.data().hasBeenChanged= true;
    	        	   }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.gcm_icon);
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
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/wishlists/destroy.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;
    	        	   String success = httpToPostRequest(url);
    	        	   if(success.equals("true")){
    	        		   remove.setVisibility(View.GONE);
    	           		   add_to_list.setVisibility(View.VISIBLE);
    	           		   SharedValue.data().hasBeenChanged= true;
    	        	   }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.gcm_icon);
    	        alert.show();

        	}
        });
        add_to_list.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v){
        	   String url = "http://"+Config.SERVER_BASE_URL+"/wishlists/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&membership_no="+memb;
        	   String success = httpToPostRequest(url);
        	   if(success.equals("true")){
        		   remove.setVisibility(View.VISIBLE);
           		   add_to_list.setVisibility(View.GONE);
           		   SharedValue.data().hasBeenChanged= true;
        	   }
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
    	        	   String url = "http://"+Config.SERVER_BASE_URL+"/orders/pickup.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&rental_id="+rental_id+"&membership_no="+memb;
    	        	   String success = httpToPostRequest(url);
    	        	   if(success.equals("true")){
	    	               pick_up.setVisibility(View.GONE);
    	        	   }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.gcm_icon);
    	        alert.show();
        	}
        });
        order_cancel.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){
        		AlertDialog alert = new AlertDialog.Builder(SingleMenuItemActivity.this).create();
    	        alert.setTitle(title);
    	        alert.setMessage("Are You Sure You want\n to cancel the order");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   String url = null;
    	        	   if(order_type.equals("P"))
    	        		   url = "http://"+Config.SERVER_BASE_URL+"/orders/order_cancel.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&rental_id="+rental_id+"&membership_no="+memb;
    	        	   else if(order_type.equals("D"))
    	        		   url = "http://"+Config.SERVER_BASE_URL+"/orders/order_cancel.json?api_key="+auth_token+"&phone="+numb+"&title_id="+title_id+"&delivery_order_id="+delivery_order_id+"&membership_no="+memb;
    	        	   String success = httpToPostRequest(url);
    	        	   if(success.equals("true")){
    	        		   //lblpickup_order.setText(pickup_order);
	    	               //pickup_layout.setVisibility(View.VISIBLE);
	    	               order_cancel.setVisibility(View.GONE);
	    	               SharedValue.data().hasBeenChanged= true;
    	        	   }
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.gcm_icon);
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

    	        Intent searchlib = new Intent(getApplicationContext(), SigninActivity.class);
        		startActivity(searchlib);
        	}
        });

        sign_up.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
    	        //Intent sign_up_call = new Intent(getApplicationContext(), CallCustomerCare.class);
        		Intent sign_up_call = new Intent(getApplicationContext(), Signup.class);
        		startActivity(sign_up_call);
        	}
        });
        
        track.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
    	        //Intent sign_up_call = new Intent(getApplicationContext(), CallCustomerCare.class);
        		String url = "http://"+Config.SERVER_BASE_URL+"/get_order_details.json?order_id="+delivery_order_id+"&api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
        		json_track = new JSONRequest();
        		json_track.execute(url);
        		
        	}
        });
        
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorListener = new ShakeEventListener();
	    mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
	        public void onShake() {
	        	long now = System.currentTimeMillis();
	        	if(now > initial_time + buffer_time_for_shake){
	        		finish();
	        	}
	        }
	    });

    }
	
	private class JSONRequest extends AsyncTask<String ,String , JSONObject>{
		protected JSONObject doInBackground(String... args){
			JSONParser jp = new JSONParser();
			JSONObject json = jp.getJSONFromUrl(args[0]);
			return json;
		}
		protected void onPostExecute(JSONObject json){
			if (json != null && !isCancelled()){
		      try {
		        // Getting Array of data
		    	  JSONArray list = json.getJSONArray("order_details");
		        //checking if the array is empty
				if (list != null && list.length()>0){
					String[] track_list = new String[list.length()];
			        // looping through All data
			        for (int i = 0; i < list.length(); i++) {
			          JSONObject c = list.getJSONObject(i);
			          track_list[i] = c.getString("message"); 
			        }
			        ContextThemeWrapper cw = new ContextThemeWrapper(SingleMenuItemActivity.this, R.style.AlertDialogTheme );
					AlertDialog.Builder builder = new AlertDialog.Builder(cw);
					builder.setTitle("Order Status")
				           .setItems(track_list,null);
					builder.setIcon(R.drawable.gcm_icon);
					builder.show();
				}else{
				}
		      }catch (JSONException e) {
		      }
			}
		}
	}
	
	private String httpToPostRequest(String url) {
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
	            Toast.makeText(getApplicationContext(), INFO,Toast.LENGTH_SHORT).show();
	            return jsonObject.getString("success");
	            }
            else
            	Toast.makeText(getApplicationContext(),"Sorry! please try again later",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Sorry! please try again later",Toast.LENGTH_SHORT).show();
        }
		return "false";
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

	private class JSONParse extends AsyncTask<String,String,JSONObject>{
	  protected void onPreExecute(){

	  }
	  protected JSONObject doInBackground(String... args){
		  try {
			branch_name = URLEncoder.encode(branch_name, "UTF-8");
		  } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				branch_name = "801";
		  }
		  String url = "http://"+Config.SERVER_BASE_URL+"/titles/check_availability.json?title_id="+title_id+"&branch_name="+branch_name;
		  JSONParser jp = new JSONParser();
		  JSONObject json = jp.getJSONFromUrl(url);
		  if (isCancelled()){
          	  return null;}
          else
			return json;
	  }
	  protected void onPostExecute(JSONObject json){
		  if (json != null && !isCancelled()){
			  TextView lblavail_text = (TextView) findViewById(R.id.avail_text);
		      ProgressBar progress_avail = (ProgressBar) findViewById(R.id.progress_avail);
			  try {
					// Getting Array of data
				  String status = json.getString("status");
				  lblavail_text.setText(status) ;
				  progress_avail.setVisibility(View.GONE);
				} catch (JSONException e) {
					e.printStackTrace();
					lblavail_text.setText("Books in circulation. Not currently available") ;
					progress_avail.setVisibility(View.GONE);
				}
		  }
	  }
	}

	private class JSONParseSummary extends AsyncTask<String,String,JSONObject>{
		  protected void onPreExecute(){

		  }
		  protected JSONObject doInBackground(String... args){
			  JSONParser jp = new JSONParser();
			  JSONObject json = jp.getJSONFromUrl(args[0]);
			  if (isCancelled()){
	          	  return null;}
	          else
				return json;
		  }
		  protected void onPostExecute(JSONObject json){
			  if (json != null && !isCancelled()){
				try {
					final TextView lblExpand = (TextView) findViewById(R.id.expand);
					lblSummary.setText(json.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getString("description"));
					
					
					final ViewTreeObserver vto = lblSummary.getViewTreeObserver();
				    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				        @SuppressLint("NewApi")
						@Override
				        public void onGlobalLayout() {
				           Layout l = lblSummary.getLayout();
				           if ( l != null){
				              int lines = l.getLineCount();
				              if ( lines > 5){
				            	  if (Build.VERSION.SDK_INT < 16){ 
										 lblSummary.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
									 }else{ 
										 lblSummary.getViewTreeObserver().removeOnGlobalLayoutListener(this); 
									 }
				            	  lblExpand.setVisibility(View.VISIBLE);
							      lblExpand.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											lblSummary.setMaxLines(Integer.MAX_VALUE);
											lblExpand.setVisibility(View.GONE);
										}
							      });
				              }
				           }  
				        }
				    });
				} catch (JSONException e) {
					e.printStackTrace();
					lblSummary.setText("summary not available");
				}
			  }
		  }
		}
	@Override
	public void onResume(){
      super.onResume();
      mSensorManager.registerListener(mSensorListener,
      mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
    }
	public void onDestroy(){
	  super.onDestroy();
	  mSensorManager.unregisterListener(mSensorListener);
	  json_parse.cancel(true);
    }
    public void onBackPressed(){
	  json_parse.cancel(true);
	  if (json_summary!=null)
		  json_summary.cancel(true);
	  mSensorManager.unregisterListener(mSensorListener);
	  finish();
    }
}