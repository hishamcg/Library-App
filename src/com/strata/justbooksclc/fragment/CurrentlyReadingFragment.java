package com.strata.justbooksclc.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
  private JSONParse json_parse = new JSONParse();;
  String auth_token;
  String memb;
  String numb;
  // contacts JSONArray
  JSONArray list = null;
  CustomAdapter adapter;
  ArrayList<Book> bookList = new ArrayList<Book>();
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    ColorDrawable gray = new ColorDrawable(this.getResources().getColor(R.color.gray));
	getListView().setDivider(gray);
	getListView().setDividerHeight(1);
  	
    SharedPreferences value = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    auth_token = value.getString("AUTH_TOKEN","");
    memb = value.getString("MEMBERSHIP_NO","");
    numb = value.getString("NUMBER","");
    //json_parse = new JSONParse();
    //json_parse.execute();
  }


  public void onListItemClick(ListView l, View view, int position, long id) {
	  Intent in = new Intent(this.getActivity().getApplicationContext(),SingleMenuItemActivity.class);
	  
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
  private class JSONParse extends AsyncTask<String,String,JSONObject>{
    protected void onPreExecute(){
      
    }
    protected JSONObject doInBackground(String... args){
      System.out.println("score");
      String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/books_at_home.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
      JSONParser jp = new JSONParser();
      JSONObject json = jp.getJSONFromUrl(url);
      return json;
    }
    @SuppressWarnings("deprecation")
	protected void onPostExecute(JSONObject json){
	    if (json != null && !isCancelled()){
	      try {
	        // Getting Array of data
	        list = json.getJSONArray(TAG_WISHLIST);
	        //checking if the array is empty 
			if (list != null){
				bookList.clear();
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
		        if(bookList.size() == 0)
					setEmptyText("Your dont hold any book");
		        adapter = new CustomAdapter(getActivity(), bookList);
			    setListAdapter(adapter);
			}else{
				setEmptyText("Your dont hold any book");
			}
	      }catch (JSONException e) {
			  setEmptyText("Your dont hold any book");
	      }
		}else{
			AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
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
	        alert.show();
	        
		}
	 }
  }
  public void onResume(){
		super.onResume();
		//if (adapter != null){
		json_parse = new JSONParse();
		json_parse.execute();
			//adapter.notifyDataSetChanged();
		//}
  }
  @Override
  public void onDestroy(){
	  super.onDestroy();
		 json_parse.cancel(true);
  }
} 