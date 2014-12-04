package com.strata.justbooksclc.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.activity.SingleMenuItemActivity;
import com.strata.justbooksclc.adapter.CustomAdapter;
import com.strata.justbooksclc.model.Book;

public class CurrentlyReadingFragment extends ListFragment {
  //private static final String SERVER_BASE_URL = "192.168.2.113:4321";
  // JSON Node names
  private static final String TAG_WISHLIST = "titles";
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
  //private JSONParse json_parse = new JSONParse();
  private JSONParse json_parse;
  String auth_token;
  String memb;
  String numb;
  // contacts JSONArray
  JSONArray list = null;
  CustomAdapter adapter;
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    ColorDrawable gray = new ColorDrawable(this.getResources().getColor(R.color.gray));
	getListView().setDivider(gray);
	getListView().setDividerHeight(1);
	
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
  	
    SharedPreferences value = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    auth_token = value.getString("AUTH_TOKEN","");
    memb = value.getString("MEMBERSHIP_NO","");
    numb = value.getString("NUMBER","");
    json_parse = new JSONParse();
    json_parse.execute();
  }


  public void onListItemClick(ListView l, View view, int position, long id) {
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
    String title_id = ((TextView) view.findViewById(R.id.title_id))
        .getText().toString();
    String rental_id = ((TextView) view.findViewById(R.id.rental_id))
        .getText().toString();
	String times_rented = ((TextView) view.findViewById(R.id.times_rented))
			.getText().toString();
	String avg_reading = ((TextView) view.findViewById(R.id.avg_reading))
			.getText().toString();
	String summary = ((TextView) view.findViewById(R.id.summary))
			.getText().toString();
	String pickup_order = ((TextView) view.findViewById(R.id.pickup_order))
			.getText().toString();
    
    // Starting new intent
    Intent in = new Intent(this.getActivity().getApplicationContext(),SingleMenuItemActivity.class);
    in.putExtra(TAG_AUTHOR, author);
    in.putExtra(TAG_CATEGORY, category);
    in.putExtra(TAG_TITLE, title);
    in.putExtra(TAG_LANGUAGE, publisher);
    in.putExtra(TAG_PAGE, price);
    in.putExtra(TAG_IMAGE_URL, image_url);
    in.putExtra(TAG_ID_call, title_id);
    in.putExtra(RENTAL_ID, rental_id);
	in.putExtra(TIMES_RENTED,times_rented);
	in.putExtra(AVG_READING,avg_reading);
	in.putExtra(SUMMARY,summary);
	in.putExtra(PICKUP_ORDER,pickup_order);
    in.putExtra("message", "current");
    in.putExtra("check","logged_in");
    
    startActivity(in);
  }
  private class JSONParse extends AsyncTask<String,String,JSONObject>{
    protected void onPreExecute(){
      
    }
    protected JSONObject doInBackground(String... args){
      System.out.println("score");
      String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/books_at_home.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
      JSONParser jp = new JSONParser();
      JSONObject json = jp.getJSONFromUrl(url);
      if (isCancelled())
    	  return null;
      else
    	  return json;
    }
    protected void onPostExecute(JSONObject json){
	    if (json != null && !isCancelled()){
	    	ArrayList<Book> bookList = new ArrayList<Book>();
	      try {
	        // Getting Array of data
	        list = json.getJSONArray(TAG_WISHLIST);
	        //checking if the array is empty 
			if (list.length() != 0){
		        // looping through All data
		        for (int i = 0; i < list.length(); i++) {
		          JSONObject c = list.getJSONObject(i);
		          
		          // Storing each json item in variable
		          String author = c.getString(TAG_AUTHOR);
		          String category = c.getString(TAG_CATEGORY);
		          String page = c.getString(TAG_PAGE);
		          String language = c.getString(TAG_LANGUAGE);
		          String title = c.getString(TAG_TITLE);
		          String summary = c.getString(SUMMARY);
		          String rental_id = c.getString(RENTAL_ID);
		          String image_url = c.getString(TAG_IMAGE_URL);
		          String title_id = c.getString(TAG_ID);
			      String times_rented = c.getString(TIMES_RENTED);
			      String avg_reading= c.getString(AVG_READING);
			      String pickup_order = c.getString(PICKUP_ORDER);
		  
		          Book book = new Book();
		          book.setTitle(title);
		          book.setAuthor(author);
		          book.setCategory(category);
		          book.setPrice(page);
		          book.setPublisher(language);
		          book.setImage_url(image_url);
		          book.setSummary(summary);
		          book.setId(title_id);
		          book.setRental_id(rental_id);
		          book.setTimes_rented(times_rented);
		          book.setAvg_reading(avg_reading);
		          book.setPickup_order(pickup_order);
		          // adding HashList to ArrayList
		          bookList.add(book);
		        }
			}else{
				setEmptyText("Your dont hold any book");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			setEmptyText("Your dont hold any book");
		}
		adapter = new CustomAdapter(getActivity(), bookList);
		// selecting single ListView item
	    setListAdapter(adapter);
		}
	 }
  }
  public void onResume(){
		super.onResume();
		if (adapter != null){
			json_parse = new JSONParse();
			json_parse.execute();
			//adapter.notifyDataSetChanged();
			}
  }
  public void onDestroy(){
	  super.onDestroy();
	 json_parse.cancel(true);
  }
  public void onBackPressed(){
	  json_parse.cancel(true);
  }
} 