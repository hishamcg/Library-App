package com.strata.justbooksclc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class NewArrivalFragment extends ListFragment {

  private ProgressDialog progress;
  //private static final String SERVER_BASE_URL = "192.168.2.113:4321";
  // JSON Node names
  private static final String TAG_WISHLIST = "titles";
  private static final String TAG_AUTHOR = "author";
  private static final String TAG_CATEGORY = "category";
  private static final String TAG_PAGE = "no_of_pages";
  private static final String TAG_IMAGE_URL = "image_url";
  private static final String SUMMARY = "summary";
  private static final String TAG_LANGUAGE = "language";
  private static final String RENTAL_ID = "rental_id";
  private static final String TAG_TITLE = "title";
  private static final String TAG_ID = "id";
  private static final String TAG_ID_call = "title_id";
  private static final String TIMES_RENTED = "no_of_times_rented";
  private static final String AVG_READING = "avg_reading_times";
  private JSONParse json_parse = new JSONParse();
  String auth_token;
  String memb;
  String numb;

	// contacts JSONArray
	JSONArray list = null;
@Override
public void onActivityCreated(Bundle savedInstanceState) {
  super.onActivityCreated(savedInstanceState);
  progress = new ProgressDialog(this.getActivity());
  
  	ColorDrawable gray = new ColorDrawable(this.getResources().getColor(R.color.gray));
	getListView().setDivider(gray);
	getListView().setDividerHeight(1);
	
	DBHelper mydb = new DBHelper(getActivity());
	ArrayList<String> db_list = new ArrayList<String>();
	db_list = mydb.getAllCotacts(1);
	List<Book> bookList = new ArrayList<Book>();
	if (db_list != null){
		for (int i=0; i < db_list.size(); i++){
			String[] mera_array = convertStringToArray(db_list.get(i));

			Book book = new Book();
			book.setImage_url(mera_array[0]);
			book.setId(mera_array[1]);
			book.setTimes_rented(mera_array[2]);
			book.setAvg_reading(mera_array[3]);
			book.setAuthor(mera_array[4]);
			book.setTitle(mera_array[5]);
			book.setCategory(mera_array[6]);
			book.setPrice(mera_array[7]);
			book.setPublisher(mera_array[8]);
			book.setSummary(mera_array[9]);
			
			// adding HashList to ArrayList
			bookList.add(book);
		}
		Book[] bookAry = new Book[bookList.size()];
		CustomAdapter adapter = new CustomAdapter(getActivity(), bookList.toArray(bookAry));
		// selecting single ListView item
	    setListAdapter(adapter);
	}
	SharedPreferences value = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
	String auth_token = value.getString("AUTH_TOKEN","");
	String memb = value.getString("MEMBERSHIP_NO","");
	String numb = value.getString("NUMBER","");
	String url;
	if (numb != null && numb != ""){
		url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
	}else{
		url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json";
	}
	json_parse.execute(url);
}


public void onListItemClick(ListView l, View view, int position, long id) {
		// getting values from selected ListItem
		String author = ((TextView) view.findViewById(R.id.author))
				.getText().toString();
		String category = ((TextView) view.findViewById(R.id.category))
				.getText().toString();
		String publisher = ((TextView) view.findViewById(R.id.publisher))
				.getText().toString();
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
		Intent in = new Intent(this.getActivity().getApplicationContext(),SingleMenuItemActivity.class);
		in.putExtra(TAG_AUTHOR, author);
		in.putExtra(TAG_CATEGORY, category);
		in.putExtra(TAG_TITLE, title);
		in.putExtra(TAG_LANGUAGE, publisher);
		in.putExtra(TAG_PAGE, price);
		in.putExtra(TAG_IMAGE_URL, image_url);
		in.putExtra(SUMMARY, summary);
		in.putExtra(TAG_ID_call, title_id);
		in.putExtra(TIMES_RENTED,times_rented);
		in.putExtra(RENTAL_ID,"");
		in.putExtra(AVG_READING,avg_reading);
		in.putExtra("message", "create");
		in.putExtra("check","logged_in");
		
		startActivity(in);
}

private class JSONParse extends AsyncTask<String,String,JSONObject>{
	  protected void onPreExecute(){
		  
	  }
	  protected JSONObject doInBackground(String... url){
		  JSONParser jp = new JSONParser();
		  JSONObject json = jp.getJSONFromUrl(url[0]);
		  if (isCancelled())
			  return null;
		  else
			  return json;
	  }
	  protected void onPostExecute(JSONObject json){
		
		if (json != null && !isCancelled()){
			List<Book> bookList = new ArrayList<Book>();
			try {
				// Getting Array of data
				list = json.getJSONArray(TAG_WISHLIST);
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
				}else{
					setEmptyText("There is no New Arrivals");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				setEmptyText("There is no New Arrivals");
			}
			Book[] bookAry = new Book[bookList.size()];
			CustomAdapter adapter = new CustomAdapter(getActivity(), bookList.toArray(bookAry));
			// selecting single ListView item
		    setListAdapter(adapter);
		}
	  }
	}
	public static String strSeparator = "__,__";
	public static String convertArrayToString(String[] array){
	    String str = "";
	    for (int i = 0;i<array.length; i++) {
	        str = str+array[i];
	        // Do not append comma at the end of last element
	        if(i<array.length-1){
	            str = str+strSeparator;
	        }
	    }
	    return str;
	}
	public static String[] convertStringToArray(String str){
	    String[] arr = str.split(strSeparator);
	    return arr;
	}
	public void onResume(){
			super.onResume();
			progress.hide();
		}
	public void onDestroy(){
		  super.onDestroy();
		 json_parse.cancel(true);
	}
} 