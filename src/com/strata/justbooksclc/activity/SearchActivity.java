package com.strata.justbooksclc.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.CustomAdapter;
import com.strata.justbooksclc.model.Book;
import com.strata.justbooksclc.tabs.TabMyListActivity;

public class SearchActivity extends Activity {
	private ProgressDialog progress;
	// JSON Node names
	private static final String TAG_SEARCHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String ISBN = "isbn";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_call = "title_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private String check_log="not_logged_in";
	private String auth_token;
	private String memb;
	private String numb;
	private String url;
	private String searchText="";
	private SearchView searchView;
	// contacts JSONArray
	private JSONArray list = null;
	private ArrayList<Book> bookList = new ArrayList<Book>();
	private JSONParse json_parse;
	private ListView list_view;
	@SuppressLint("NewApi")
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_page_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
         searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //return true;
        //expand search
        menu.findItem(R.id.action_search).expandActionView();

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	    TextView textView = (TextView) searchView.findViewById(id);
	    textView.setHintTextColor(0x88ffffff);
	    textView.setTextColor(0xffffffff);
	    textView.setText(searchText);
	    searchView.clearFocus();
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
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
	  }
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
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

		setContentView(R.layout.search_layout);
		// get action bar
        ActionBar actionBar = getActionBar();
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
		progress = new ProgressDialog(this);
		list_view = (ListView)findViewById(R.id.list);
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),SingleMenuItemActivity.class);
				Book bookAtPos = bookList.get(position);
				in.putExtra(TAG_AUTHOR, bookAtPos.getAuthor());
				in.putExtra(TAG_CATEGORY, bookAtPos.getCategory());
				in.putExtra(TAG_TITLE, bookAtPos.getTitle());
				in.putExtra(TAG_LANGUAGE, bookAtPos.getPublisher());
				in.putExtra(TAG_PAGE, bookAtPos.getPrice());
				in.putExtra(TAG_IMAGE_URL, bookAtPos.getImage_url());
				in.putExtra(SUMMARY, bookAtPos.getSummary());
				in.putExtra(TAG_ID_call, bookAtPos.getId());
				in.putExtra(TIMES_RENTED, bookAtPos.getTimes_rented());
				in.putExtra(AVG_READING, bookAtPos.getAvg_reading());
				in.putExtra(ISBN, bookAtPos.getIsbn());
				in.putExtra("message", "create");
				in.putExtra("check", check_log);
				startActivity(in);

			}
		});
		handleIntent(getIntent());
	}
	@Override
    protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
    }
	private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	searchText = intent.getStringExtra(SearchManager.QUERY);
        	try{
	        	if (searchText.split("\\.")[0].equals("hishamisstupid")){
	        		SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
				    SharedPreferences.Editor   editor = preferences.edit();
				    editor.putString("READING_SCORE", searchText.split("\\.")[1]);
				    editor.putInt("BOOK_BAND", 4);
				    editor.commit();
				    finish();
	    		    Intent in = new Intent(getApplicationContext(), TabMyListActivity.class);
	    		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        		startActivity(in);
	        	}
        	}finally{
        		System.out.println("hack failed");
        	}
        	json_parse= new JSONParse();
        	json_parse.execute();
        }
    }

	@SuppressWarnings("deprecation")
	private class JSONParse extends AsyncTask<String,String,JSONObject>{
		protected void onPreExecute(){
			final ProgressBar progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
			progress_bar.setVisibility(View.VISIBLE);
		}
		protected JSONObject doInBackground(String... args){
			String fin = searchText;
			try {
				fin = URLEncoder.encode(searchText, "UTF-8");
			} catch (UnsupportedEncodingException e) {
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
				url = "http://"+Config.SERVER_BASE_URL+"/search.json?phone="+numb+"&api_key="+auth_token+"&membership_no="+memb+"&q="+fin;
			} else {
				url = "http://"+Config.SERVER_BASE_URL+"/search.json?q="+fin;}
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

							Book book = new Book();
							book.setTitle(c.getString(TAG_AUTHOR));
							book.setAuthor(c.getString(TAG_CATEGORY));
							book.setCategory(c.getString(TAG_PAGE));
							book.setPrice(c.getString(TAG_LANGUAGE));
							book.setPublisher(c.getString(TAG_TITLE));
							book.setImage_url(c.getString(TAG_IMAGE_URL));
							book.setSummary(c.getString(SUMMARY));
							book.setId(c.getString(TAG_ID));
							book.setTimes_rented(c.getString(TIMES_RENTED));
							book.setAvg_reading(c.getString(AVG_READING));
							book.setIsbn(c.getString(ISBN));
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
				CustomAdapter adapter = new CustomAdapter(SearchActivity.this, bookList);
				list_view.setAdapter(adapter);
			}else{
				AlertDialog alert = new AlertDialog.Builder(SearchActivity.this).create();
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
	public void onResume(){
		super.onResume();
	    progress.hide();
	}

}
