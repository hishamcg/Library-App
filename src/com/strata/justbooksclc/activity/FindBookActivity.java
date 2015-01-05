package com.strata.justbooksclc.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.CustomAdapter;
import com.strata.justbooksclc.model.Book;

public class FindBookActivity extends Activity{
	
	//private static final String SERVER_BASE_URL = "192.168.2.113:4321";
	// JSON Node names
	private static final String TAG_SEARCHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_call = "title_id";
	private static final String RENTAL_ID = "rental_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private static final String PICKUP_ORDER = "pickup_order_id";
	private static final String SUMMARY = "summary";

	private JSONParse json_parse = new JSONParse();
	private JSONFilter json_filter;
	private ListView list_view;
	private Spinner spinner_type;
	private Spinner spinner_name;
	private LinearLayout LL_filter;
	private ProgressBar progress;
	private Button btn_filter;
	String auth_token;
	String memb;
	String numb;
	// contacts JSONArray
	JSONArray list = null;
	CustomAdapter adapter;
	private ArrayList<Book> bookList = new ArrayList<Book>();
	JSONArray filter_type;
	ArrayList<String> all_filter_type = new ArrayList<String>();
	ArrayList<String> all_filter_name = new ArrayList<String>();
	ArrayAdapter<String> adapter_name;
	JSONObject filter;
	String selected_id = "2";
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.find_book_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    if (itemId == android.R.id.home){
	    	finish();
	    	return true;
	    }else if (itemId == R.id.action_filter){
	    	if(LL_filter.getVisibility()==View.GONE){
	    		LL_filter.setVisibility(View.VISIBLE);
	    		item.setIcon(R.drawable.ic_filter_inv);
	    	}else{
	    		LL_filter.setVisibility(View.GONE);
	    		item.setIcon(R.drawable.ic_filter);
	    	}
	    	return true;
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
	}
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//settingup new relic
		NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String my_theme = value.getString("MY_THEME", "");
		auth_token = value.getString("AUTH_TOKEN","");
		memb = value.getString("MEMBERSHIP_NO","");
		numb = value.getString("NUMBER","");
			
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
		
		setContentView(R.layout.find_book_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		spinner_type = (Spinner)findViewById(R.id.category_type);
		spinner_name = (Spinner)findViewById(R.id.name);
		LL_filter = (LinearLayout)findViewById(R.id.LL_filter);
	  	list_view = (ListView)findViewById(R.id.list);
	  	progress = (ProgressBar)findViewById(R.id.progressBar1);
	  	btn_filter = (Button)findViewById(R.id.btn_filter);
	  	//fetch all filter
	  	json_filter = new JSONFilter();
	  	json_filter.execute();
	    //set initial view
	  	String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/find_book.json?category_id="+2;
    	json_parse = new JSONParse();
    	json_parse.execute(url);
    	
    	btn_filter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url;
				if(memb!=null && memb!="")
					url = "http://"+Config.SERVER_BASE_URL+"/api/v1/find_book.json?category_id="
								   +selected_id+"&api_key="+auth_token
								   +"&phone="+numb+"&membership_no="+memb;
				else
					url = "http://"+Config.SERVER_BASE_URL+"/api/v1/find_book.json?category_id="
							   +selected_id;
				
		    	json_parse = new JSONParse();
		    	json_parse.execute(url);
		    	LL_filter.setVisibility(View.GONE);
			}
		});
	    list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent in = new Intent(getApplicationContext(),SingleMenuItemActivity.class);
				  
				  in.putExtra(TAG_AUTHOR, bookList.get(position).getAuthor());
				  in.putExtra(TAG_CATEGORY, bookList.get(position).getCategory());
				  in.putExtra(TAG_TITLE, bookList.get(position).getTitle());
				  in.putExtra(TAG_LANGUAGE, bookList.get(position).getPublisher());
				  in.putExtra(TAG_PAGE, bookList.get(position).getPrice());
				  in.putExtra(TAG_IMAGE_URL, bookList.get(position).getImage_url());
				  in.putExtra(SUMMARY, bookList.get(position).getSummary());
				  in.putExtra(RENTAL_ID, bookList.get(position).getRental_id());
				  in.putExtra(PICKUP_ORDER,bookList.get(position).getPickup_order());
				  in.putExtra(TAG_ID_call, bookList.get(position).getId());
				  in.putExtra(TIMES_RENTED, bookList.get(position).getTimes_rented());
				  in.putExtra(AVG_READING, bookList.get(position).getAvg_reading());
				  in.putExtra("message", "current");
				  in.putExtra("check","logged_in");
			  
				  startActivity(in);
			}
		});
	    
	    spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				try {
					JSONArray filter_name;
					filter_name = filter.getJSONArray(all_filter_type.get(position));
					all_filter_name.clear();
			        for (int i = 0; i < filter_name.length(); i++) {
			        	all_filter_name.add(filter_name.getJSONObject(i).getString("name"));
			        }
			        adapter_name.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    spinner_name.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				try {
					JSONObject filter_obj = filter.getJSONArray(all_filter_type.get(spinner_type.getSelectedItemPosition()))
							   .getJSONObject(position);
					selected_id = filter_obj.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	private class JSONFilter extends AsyncTask<String,String,JSONObject>{
		
	    protected JSONObject doInBackground(String... args){
	      String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/category.json";
	      JSONParser jp = new JSONParser();
	      JSONObject json = jp.getJSONFromUrl(url);
	      return json;
	    }
		protected void onPostExecute(JSONObject json){
		    if (json != null && !isCancelled()){
		      try {
		        // Getting Array of data
		        filter = json.getJSONObject("category");
		        filter_type = filter.getJSONArray("type");
		        for (int i = 0; i < filter_type.length(); i++) {
		        	all_filter_type.add(filter_type.getString(i));
		        }
		        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(getBaseContext(),R.layout.spinner_item,all_filter_type);
	    		// Specify the layout to use when the list of choices appears
		        adapter_type.setDropDownViewResource(R.layout.spinner_item);
	    		// Apply the adapter to the spinner
	    		spinner_type.setAdapter(adapter_type);
	    		
	    		JSONArray filter_name = filter.getJSONArray(all_filter_type.get(0));
		        all_filter_name.clear();
		        for (int i = 0; i < filter_name.length(); i++) {
		        	all_filter_name.add(filter_name.getJSONObject(i).getString("name"));
		        }
		        
		        adapter_name = new ArrayAdapter<String>(getBaseContext(),R.layout.spinner_item,all_filter_name);
	    		// Specify the layout to use when the list of choices appears
		        adapter_name.setDropDownViewResource(R.layout.spinner_item);
	    		// Apply the adapter to the spinner
	    		spinner_name.setAdapter(adapter_name);
		      }catch (JSONException e) {
		      }
			}
		 }
	  }
	
	private class JSONParse extends AsyncTask<String,String,JSONObject>{
	    protected void onPreExecute(){
	    	progress.setVisibility(View.VISIBLE);
	    }
	    protected JSONObject doInBackground(String... args){
	      JSONParser jp = new JSONParser();
	      JSONObject json = jp.getJSONFromUrl(args[0]);
	      return json;
	    }
		@SuppressWarnings("deprecation")
		protected void onPostExecute(JSONObject json){
			if (json != null ){
				try {
					// Getting Array of Books
					list = json.getJSONArray(TAG_SEARCHLIST);
					//checking if the array is empty
					if (list.length() != 0){
						// looping through All Books
						bookList.clear();
						for (int i = 0; i < list.length(); i++) {
							JSONObject c = list.getJSONObject(i);

							// Storing each json item in variable
							String author = c.getString(TAG_AUTHOR);
							String category = c.getString(TAG_CATEGORY);
							String page = c.getString(TAG_PAGE);
							String language = c.getString(TAG_LANGUAGE);
							String title = c.getString(TAG_TITLE);
							String image_url = c.getString(TAG_IMAGE_URL);
							String summary = c.getString(SUMMARY);
							String title_id = c.getString(TAG_ID);
					        String times_rented = c.getString(TIMES_RENTED);
					        String avg_reading= c.getString(AVG_READING);


							Book book = new Book();
							book.setTitle(title);
							book.setAuthor(author);
							book.setCategory(category);
							book.setPrice(page);
							book.setPublisher(language);
							book.setImage_url(image_url);
							book.setSummary(summary);
							book.setId(title_id);
							book.setTimes_rented(times_rented);
							book.setAvg_reading(avg_reading);

							// adding HashList to ArrayList
							bookList.add(book);
						}
					}
					else{
						Toast.makeText(getApplicationContext(),"No Data To Display",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"No Data To Display",Toast.LENGTH_SHORT).show();
				}
				progress.setVisibility(View.GONE);
				CustomAdapter adapter = new CustomAdapter(FindBookActivity.this, bookList);
				list_view.setAdapter(adapter);
			}else{
				AlertDialog alert = new AlertDialog.Builder(getBaseContext()).create();
		        alert.setTitle("Connection Time Out!");
		        alert.setMessage("We were not able to reach the server. Please try again after some time");
		        alert.setButton("Retry", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int which) {
			        	   json_parse = new JSONParse();
			        	   json_parse.execute();
			           }
			        });
		        // Set the Icon for the Dialog
		        alert.setIcon(R.drawable.gcm_icon);
		        alert.setCancelable(false);
		        alert.show();
			}
		 }
	  }
	
	@Override
	  public void onDestroy(){
		  super.onDestroy();
			 json_parse.cancel(true);
	  }
}
