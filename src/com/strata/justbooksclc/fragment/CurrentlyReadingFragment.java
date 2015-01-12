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
  private static final String ISBN = "isbn";
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
	  Book bookAtPos = bookList.get(position);
	  in.putExtra(TAG_AUTHOR, bookAtPos.getAuthor());
	  in.putExtra(TAG_CATEGORY, bookAtPos.getCategory());
	  in.putExtra(TAG_TITLE, bookAtPos.getTitle());
	  in.putExtra(TAG_LANGUAGE, bookAtPos.getPublisher());
	  in.putExtra(TAG_PAGE, bookAtPos.getPrice());
	  in.putExtra(TAG_IMAGE_URL, bookAtPos.getImage_url());
	  in.putExtra(SUMMARY, bookAtPos.getSummary());
	  in.putExtra(ISBN, bookAtPos.getIsbn());
	  in.putExtra(RENTAL_ID, bookAtPos.getRental_id());
	  in.putExtra(PICKUP_ORDER,bookAtPos.getPickup_order());
	  in.putExtra(TAG_ID_call, bookAtPos.getId());
	  in.putExtra(TIMES_RENTED, bookAtPos.getTimes_rented());
	  in.putExtra(AVG_READING, bookAtPos.getAvg_reading());
	  in.putExtra("message", "current");
	  in.putExtra("check","logged_in");

	  startActivity(in);
  }
  private class JSONParse extends AsyncTask<String,String,JSONObject>{
    protected void onPreExecute(){

    }
    protected JSONObject doInBackground(String... args){
      String url = "http://"+Config.SERVER_BASE_URL+"/books_at_home.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
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

		          Book book = new Book();
		          book.setTitle(c.getString(TAG_AUTHOR));
		          book.setAuthor(c.getString(TAG_CATEGORY));
		          book.setCategory(c.getString(TAG_PAGE));
		          book.setPrice(c.getString(TAG_LANGUAGE));
		          book.setPublisher(c.getString(TAG_TITLE));
		          book.setImage_url(c.getString(SUMMARY));
		          book.setSummary(c.getString(RENTAL_ID));
		          book.setId(c.getString(TAG_IMAGE_URL));
		          book.setRental_id(c.getString(TAG_ID));
		          book.setIsbn(c.getString(ISBN));
		          book.setTimes_rented(c.getString(TIMES_RENTED));
		          book.setAvg_reading(c.getString(AVG_READING));
		          book.setPickup_order(c.getString(PICKUP_ORDER));
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