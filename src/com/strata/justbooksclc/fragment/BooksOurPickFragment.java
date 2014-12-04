package com.strata.justbooksclc.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.strata.justbooksclc.DBHelper;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.activity.SingleMenuItemActivity;
import com.strata.justbooksclc.adapter.CustomAdapter;
import com.strata.justbooksclc.model.Book;

public class BooksOurPickFragment extends ListFragment {
	//private static final String SERVER_BASE_URL = "192.168.2.113:4321";
	// JSON Node names
	private static final String TAG_WISHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "id";
	private static final String RENTAL_ID = "rental_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	//private JSONParse json_parse = new JSONParse();
	private JSONParse json_parse;
	
	CustomAdapter adapter;
	// contacts JSONArray
	JSONArray list = null;
	private DBHelper mydb;
	ArrayList<Book> bookList = new ArrayList<Book>();
	String data_fetch_url;
	int db_table_tagid;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mydb = new DBHelper(getActivity());
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	data_fetch_url = getArguments().getString("url");
	db_table_tagid = getArguments().getInt("db_table_key");
	UpdateListFromDatabase(db_table_tagid);
	if (adapter != null && !data_fetch_url.isEmpty()){
		json_parse = new JSONParse();
		json_parse.execute(data_fetch_url);
	}
	
  }
  public void onListItemClick(ListView l, View view, int position, long id) {
	  Intent in = new Intent(getActivity().getApplicationContext(),SingleMenuItemActivity.class);
	  in.putExtra(TAG_AUTHOR, bookList.get(position).getAuthor());
      in.putExtra(TAG_CATEGORY, bookList.get(position).getCategory());
      in.putExtra(TAG_TITLE, bookList.get(position).getTitle());
      in.putExtra(TAG_LANGUAGE, bookList.get(position).getPublisher());
      in.putExtra(TAG_PAGE, bookList.get(position).getPrice());
      in.putExtra(TAG_IMAGE_URL, bookList.get(position).getImage_url());
      in.putExtra(SUMMARY, bookList.get(position).getSummary());
      in.putExtra(RENTAL_ID, "");
      in.putExtra("title_id", bookList.get(position).getId());
      in.putExtra(TIMES_RENTED, bookList.get(position).getTimes_rented());
      in.putExtra(AVG_READING, bookList.get(position).getAvg_reading());
		in.putExtra("message", "create");
		in.putExtra("check","logged_in");
		startActivity(in);
  }
  private class JSONParse extends AsyncTask<String,String,JSONObject>{
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
				// Getting Array of data
				list = json.getJSONArray(TAG_WISHLIST);
				if(list != null){
					String[] bookArrayStringed = new String[list.length()+1];
					bookList.clear();
					// looping through All data
					for (int i = 0; i < list.length(); i++) {
						String[] temp_array = new String[10];
						JSONObject c = list.getJSONObject(i);
		
						temp_array[0] = c.getString(TAG_TITLE);
						temp_array[1] = c.getString(TAG_AUTHOR);
						temp_array[2] = c.getString(TAG_CATEGORY);
						temp_array[3] = c.getString(TAG_PAGE);
						temp_array[4] = c.getString(TAG_LANGUAGE);
						temp_array[5] = c.getString(TAG_IMAGE_URL);
						temp_array[6] = c.getString(SUMMARY);
						temp_array[7] = c.getString(TAG_ID);
						temp_array[8] = c.getString(TIMES_RENTED);
						temp_array[9] = c.getString(AVG_READING);
						bookArrayStringed[i] = convertArrayToString(temp_array);
						
						Book book = new Book();
						book.setTitle(temp_array[0]);
						book.setAuthor(temp_array[1]);
						book.setCategory(temp_array[2]);
						book.setPrice(temp_array[3]);
						book.setPublisher(temp_array[4]);
						book.setImage_url(temp_array[5]);
						book.setSummary(temp_array[6]);
						book.setId(temp_array[7]);
						book.setTimes_rented(temp_array[8]);
						book.setAvg_reading(temp_array[9]);
						// adding HashList to ArrayList
						bookList.add(book);
					 }
					mydb.insertAllData(bookArrayStringed,db_table_tagid);
					adapter.notifyDataSetChanged();
				}else{
					setEmptyText("No books to show");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				setEmptyText("No books to show");
			}
			
		}
	  }
  }
  
  private void UpdateListFromDatabase(int slot){
		int l_count = 0;
		ArrayList<String> db_list = new ArrayList<String>();
		db_list = mydb.getAllCotacts(slot);
		if (db_list != null && !db_list.isEmpty()){
			l_count = db_list.size();
			String[] temp_array = new String[10];
			//shake_array_length += l_count;
			for (int i=0; i < l_count; i++){
				temp_array = convertStringToArray(db_list.get(i));
				Book book = new Book();
				book.setTitle(temp_array[0]);
				book.setAuthor(temp_array[1]);
				book.setCategory(temp_array[2]);
				book.setPrice(temp_array[3]);
				book.setPublisher(temp_array[4]);
				book.setImage_url(temp_array[5]);
				book.setSummary(temp_array[6]);
				book.setId(temp_array[7]);
				book.setTimes_rented(temp_array[8]);
				book.setAvg_reading(temp_array[9]);
				// adding HashList to ArrayList
				bookList.add(book);
			}
		}else{
			setEmptyText("No books to show");
		}
		adapter = new CustomAdapter(getActivity(), bookList);
		// selecting single ListView item
	    setListAdapter(adapter);
		
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
    public void onDestroy(){
	    super.onDestroy();
	    if (json_parse != null){
	    	json_parse.cancel(true);
	    }
	    mydb.close();
    }
} 
