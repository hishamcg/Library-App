package com.example.myfirstapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ProgressDialog progress;
	// JSON Node names
	private static final String SEARCH_URL = "url";
	private static final String TAG_SEARCHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ISBN = "isbn";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_call = "title_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private String check_log="not_logged_in";
	String auth_token;
	String memb;
	String numb;
	String url;
	String searchText="";
	Boolean where_to = true;
	
	// contacts JSONArray
	JSONArray list = null;
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        //menuInflater.inflate(R.menu.activity_main_actions, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         SearchView searchView =
                 (SearchView) menu.findItem(R.id.action_search).getActionView();
         searchView.setSearchableInfo(
                 searchManager.getSearchableInfo(getComponentName()));
        //return true;
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
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
			/*progress = new ProgressDialog(this);
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
			}*/
	  }
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// get action bar   
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
		progress = new ProgressDialog(this);
//		final Button my_Button = (Button) findViewById(R.id.button1);
		handleIntent(getIntent());
//        my_Button.setOnClickListener(new Button.OnClickListener() {
//    	   public void onClick(View v){
//   	        searchText = ((EditText) findViewById(R.id.editText1)).getText().toString();
//       		Log.i("searchTExt", searchText);
//       		where_to = false;
//       		new JSONParse().execute();
//       		
////	       		Intent moresearch = new Intent(getApplicationContext(), MainActivity.class);
////	       		moresearch.putExtra(SEARCH_URL, searchText);
////	       		moresearch.putExtra("check",check_log);
////	       		startActivity(moresearch);
//    	   }
//        });
//        new JSONParse().execute();
	}
	@Override
    protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
    }
	private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	searchText = intent.getStringExtra(SearchManager.QUERY);
            new JSONParse().execute();
        }
    }
	
	private class JSONParse extends AsyncTask<String,String,JSONObject>{
		protected void onPreExecute(){
			final ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
			progress_bar.setVisibility(View.VISIBLE);
		}
		protected JSONObject doInBackground(String... args){
			String fin = searchText;
//			if (where_to){
//				Intent search_val = getIntent();
//				fin = search_val.getStringExtra(SEARCH_URL);
//				check_log = search_val.getStringExtra("check");
//			}
//			else{
//				fin = searchText;
//			}
			try {
				//fin = URLEncoder.encode(fin, "UTF-8");
				fin = URLEncoder.encode(searchText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			System.out.println("score");
			if (!check_log.equals("not_logged_in")){
				SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
				auth_token = value.getString("AUTH_TOKEN","");
				memb = value.getString("MEMBERSHIP_NO","");
				numb = value.getString("NUMBER","");
				url = "http://"+Config.SERVER_BASE_URL+"/api/v1/search.json?phone="+numb+"&api_key="+auth_token+"&membership_no="+memb+"&q="+fin;
			} else {
				url = "http://"+Config.SERVER_BASE_URL+"/api/v1/search.json?q="+fin;}
			System.out.println("score");
			
			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();
			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);
			return json;
		}
		protected void onPostExecute(JSONObject json){
			final ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
			progress_bar.setVisibility(View.GONE);
			// Hashmap for ListView
			List<Book> bookList = new ArrayList<Book>();
			if (json != null ){
				try {
					// Getting Array of Books
					list = json.getJSONArray(TAG_SEARCHLIST);
					//checking if the array is empty 
					if (list.length() != 0){
						// looping through All Books
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
						Toast.makeText(getApplicationContext(),"No Data To Display",Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(),"No Data To Display",Toast.LENGTH_LONG).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext(),"No Data To Display",Toast.LENGTH_LONG).show();
			}
			Book[] bookAry = new Book[bookList.size()];
			CustomAdapter adapter = new CustomAdapter(MainActivity.this, bookList.toArray(bookAry));
			ListView lv = (ListView)findViewById(R.id.list);
			lv.setAdapter(adapter);
			// Launching new screen on Selecting Single ListItem
			lv.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// getting values from selected ListItem
					String author = ((TextView) view.findViewById(R.id.author))
							.getText().toString();
					String category = ((TextView) view.findViewById(R.id.category))
							.getText().toString();
					String publisher = ((TextView) view
							.findViewById(R.id.publisher)).getText().toString();
					String price = ((TextView) view.findViewById(R.id.price))
							.getText().toString();
					String title = ((TextView) view.findViewById(R.id.title))
							.getText().toString();
					String image_url = ((TextView) view.findViewById(R.id.image_url))
							.getText().toString();
					String summary = ((TextView) view.findViewById(R.id.summary))
							.getText().toString();
					String title_id = ((TextView) view.findViewById(R.id.title_id))
							.getText().toString();
					String times_rented = ((TextView) view.findViewById(R.id.times_rented))
							.getText().toString();
					String avg_reading = ((TextView) view.findViewById(R.id.avg_reading))
							.getText().toString();
					
					// Starting new intent
					Intent in = new Intent(getApplicationContext(),
					SingleMenuItemActivity.class);
					in.putExtra(TAG_AUTHOR, author);
					in.putExtra(TAG_CATEGORY, category);
					in.putExtra(TAG_TITLE, title);
					in.putExtra(TAG_LANGUAGE, publisher);
					in.putExtra(TAG_PAGE, price);
					in.putExtra(TAG_IMAGE_URL, image_url);
					in.putExtra(SUMMARY, summary);
					in.putExtra(TAG_ID_call, title_id);
					in.putExtra(TIMES_RENTED,times_rented);
					in.putExtra(AVG_READING,avg_reading);
					in.putExtra("message", "create");
					in.putExtra("check", check_log);
					startActivity(in);

				}
			});
		}
	}
	@Override
	public void onResume(){
		super.onResume();
	    progress.hide();
	}

}
