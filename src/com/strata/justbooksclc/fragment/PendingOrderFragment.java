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
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.activity.SingleMenuItemActivity;
import com.strata.justbooksclc.adapter.CustomAdapter;
import com.strata.justbooksclc.model.Book;

public class PendingOrderFragment extends ListFragment {
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
  private static final String SUMMARY = "summary";
  private static final String AllowCancel = "allow_cancel";
  private static final String DisallowCancelReason = "disallow_cancel_reason";
  private static final String OrderType = "order_type";
  private static final String Status = "status";
  private static final String DeliveryOrderId = "delivery_order_id";
  //private JSONParse json_parse = new JSONParse();
  private JSONParse json_parse;
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
	
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
  	
    SharedPreferences value = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    auth_token = value.getString("AUTH_TOKEN","");
    memb = value.getString("MEMBERSHIP_NO","");
    numb = value.getString("NUMBER","");
    //json_parse = new JSONParse();
    //json_parse.execute();
  }


  public void onListItemClick(ListView l, View view, int position, long id) {
    // Starting new intent
    Intent in = new Intent(this.getActivity().getApplicationContext(),SingleMenuItemActivity.class);
    
    in.putExtra(TAG_AUTHOR, bookList.get(position).getAuthor());
    in.putExtra(TAG_CATEGORY, bookList.get(position).getCategory());
    in.putExtra(TAG_TITLE, bookList.get(position).getTitle());
    in.putExtra(TAG_LANGUAGE, bookList.get(position).getPublisher());
    in.putExtra(TAG_PAGE, bookList.get(position).getPrice());
    in.putExtra(TAG_IMAGE_URL, bookList.get(position).getImage_url());
    in.putExtra(SUMMARY, bookList.get(position).getSummary());
    in.putExtra(RENTAL_ID, bookList.get(position).getRental_id());
    in.putExtra(TAG_ID_call, bookList.get(position).getId());
    in.putExtra(TIMES_RENTED, bookList.get(position).getTimes_rented());
    in.putExtra(AVG_READING, bookList.get(position).getAvg_reading());
    in.putExtra(DeliveryOrderId, bookList.get(position).getDelivery_order_id());
    
    in.putExtra(AllowCancel, bookList.get(position).getAllow_cancel());
    in.putExtra(DisallowCancelReason, bookList.get(position).getDisallow_cancel_reason());
    in.putExtra(OrderType, bookList.get(position).getOrder_type());
    in.putExtra(Status, bookList.get(position).getStatus());
    in.putExtra("message", "pending");
    
    startActivity(in);
  }
  private class JSONParse extends AsyncTask<String,String,JSONObject>{
    protected void onPreExecute(){
      
    }
    protected JSONObject doInBackground(String... args){
      String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/delivery_order.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
      JSONParser jp = new JSONParser();
      JSONObject json = jp.getJSONFromUrl(url);
      if (isCancelled())
    	  return null;
      else
    	  return json;
    }
    @SuppressWarnings("deprecation")
	protected void onPostExecute(JSONObject json){
	    
	    if (json != null){
	      try {
	        // Getting Array of data
	        list = json.getJSONArray(TAG_WISHLIST);
	        //checking if the array is empty 
			if (list.length() != 0){
				bookList.clear();
		        // looping through All data
		        for (int i = 0; i < list.length(); i++) {
		          JSONObject c = list.getJSONObject(i);
		          
		          // Storing each json item in variable
		  
		          Book book = new Book();
		          book.setTitle(c.getString(TAG_TITLE));
		          book.setAuthor(c.getString(TAG_AUTHOR));
		          book.setCategory(c.getString(TAG_CATEGORY));
		          book.setPrice(c.getString(TAG_PAGE));
		          book.setPublisher(c.getString(TAG_LANGUAGE));
		          book.setImage_url(c.getString(TAG_IMAGE_URL));
		          book.setSummary(c.getString(SUMMARY));
		          book.setId(c.getString(TAG_ID));
		          book.setTimes_rented(c.getString(TIMES_RENTED));
		          book.setAvg_reading(c.getString(AVG_READING));
		          book.setRental_id(c.getString(RENTAL_ID));
		          book.setAllow_cancel(c.getBoolean(AllowCancel));
		          book.setDisallow_cancel_reason(c.getString(DisallowCancelReason));
		          book.setOrder_type(c.getString(OrderType));
		          book.setDelivery_order_id(c.getString(DeliveryOrderId));
		          book.setStatus(c.getString(Status));
		          // adding HashList to ArrayList
		          bookList.add(book);
		        }
			}
			else{
				if(!isCancelled())
					setEmptyText("You dont have any Pending Orders");
			}
	      }catch (JSONException e) {
	    	  if(!isCancelled())
	    		  setEmptyText("You dont have any Pending Orders");
	      }
	      if(!isCancelled()){
		      adapter = new CustomAdapter(getActivity(), bookList);
		      setListAdapter(adapter);
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
	        alert.setCancelable(false);
	        alert.show();
	        
		}
	 }
  }
  public void onResume(){
	super.onResume();
	//if (adapter != null){
		json_parse = new JSONParse();
		json_parse.execute();
	//}
  }
  @Override
  public void onDestroy(){
	  super.onDestroy();
	  if(json_parse != null)
		  json_parse.cancel(true);
  }
} 