package com.example.myfirstapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ProgressDialog progress;
	private static final String SERVER_BASE_URL = "staging.justbooksclc.com:8787";
	
	// JSON Node names
	private static final String SEARCH_URL = "url";
	private static final String TAG_SEARCHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ISBN = "isbn";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_call = "title_id";
	
	Intent searchlib = getIntent();
	String fin = searchlib.getStringExtra(SEARCH_URL);
	String check_log = searchlib.getStringExtra("check");
	// contacts JSONArray
	JSONArray list = null;
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		if (!check_log.equals("not_logged_in")){
		MenuInflater menuInflater = getMenuInflater();
	    menuInflater.inflate(R.menu.front_page, menu);
	    menuInflater.inflate(R.menu.search_page_menu, menu);
	    //return true;
	    return super.onCreateOptionsMenu(menu);}
		else
			return false;
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		if (!check_log.equals("not_logged_in")){
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
		}else
			return false;
	  } 
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);
		progress.hide();
		
		 final Button my_Button = (Button) findViewById(R.id.button1);
       
           my_Button.setOnClickListener(new Button.OnClickListener() {
    	   public void onClick(View v){
    		progress.setMessage("Loading");
   	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   	        progress.setIndeterminate(true);
   	        progress.show();
       		String searchText = ((EditText) findViewById(R.id.editText1)).getText().toString();
       		Log.i("searchTExt", searchText);
       		Intent signup = new Intent(getApplicationContext(), MainActivity.class);
       		signup.putExtra(SEARCH_URL, searchText);
       		
       		startActivity(signup);
    	   }
       });
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		System.out.println("score");

		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String auth_token = value.getString("AUTH_TOKEN","");
		String memb = value.getString("MEMBERSHIP_NO","");
		String numb = value.getString("NUMBER","");
		
		System.out.println("score");
		
		// Hashmap for ListView
		List<Book> bookList = new ArrayList<Book>();
		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();
		// getting JSON string from URL
		
		try {
			String url = "http://"+SERVER_BASE_URL+"/api/v1/search.json?phone="+numb+"&api_key="+auth_token+"&membership_no="+memb+"&q="+URLEncoder.encode(fin, "UTF-8");
			JSONObject json = jParser.getJSONFromUrl(url);
			// Getting Array of Books
			list = json.getJSONArray(TAG_SEARCHLIST);

			// looping through All Books
			for (int i = 0; i < list.length(); i++) {
				JSONObject c = list.getJSONObject(i);
				
				// Storing each json item in variable
				String author = c.getString(TAG_AUTHOR);
				String category = c.getString(TAG_CATEGORY);
				String page = c.getString(TAG_PAGE);
				String language = c.getString(TAG_LANGUAGE);
				String title = c.getString(TAG_TITLE);
				String isbn = c.getString(TAG_ISBN);
				String title_id = c.getString(TAG_ID);
				//String imageUrl = "http://cdn2.justbooksclc.com/medium/"+isbn+".jpg";

				// Phone number is agin JSON Object
				/*
				 * JSONObject phone = c.getJSONObject(TAG_PHONE); String mobile
				 * = phone.getString(TAG_PHONE_MOBILE); String home =
				 * phone.getString(TAG_PHONE_HOME); String office =
				 * phone.getString(TAG_PHONE_OFFICE);
				 */

				Book book = new Book();
				book.setTitle(title);
				book.setAuthor(author);
				book.setCategory(category);
				book.setPrice(page);
				book.setPublisher(language);
				//book.setImageUrl("http://"+SERVER_BASE_URL+"/assets/"+imageUrl);
				book.setImageUrl("http://cdn2.justbooksclc.com/medium/"+isbn+".jpg");
				book.setIsbn(isbn);
				book.setId(title_id);
				
				// adding HashList to ArrayList
				bookList.add(book);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * Updating parsed JSON data into ListView
		 * */
		Book[] bookAry = new Book[bookList.size()];
		CustomAdapter adapter = new CustomAdapter(this, bookList.toArray(bookAry));
//		setListAdapter(adapter);
		// selecting single ListView item
		ListView lv = (ListView)findViewById(R.id.list);
		lv.setAdapter(adapter);
		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				progress.setMessage("Loading");
    	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	        progress.setIndeterminate(true);
    	        progress.show();
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
				String isbn = ((TextView) view.findViewById(R.id.isbn))
						.getText().toString();
				String title_id = ((TextView) view.findViewById(R.id.title_id))
						.getText().toString();
				System.out.println("########isbn#######"+isbn);
				System.out.println("#######title#######"+title);
				System.out.println("########publisher#######"+publisher);
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SingleMenuItemActivity.class);
				in.putExtra(TAG_AUTHOR, author);
				in.putExtra(TAG_CATEGORY, category);
				in.putExtra(TAG_TITLE, title);
				in.putExtra(TAG_LANGUAGE, publisher);
				in.putExtra(TAG_PAGE, price);
				in.putExtra(TAG_IMAGE_URL, "http://cdn2.justbooksclc.com/medium/"+isbn+".jpg");
				in.putExtra("message", "create");
				in.putExtra(TAG_ID_call, title_id);
				in.putExtra("check", check_log);
				startActivity(in);

			}
		});

	}
	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}

}
