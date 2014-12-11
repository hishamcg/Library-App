package com.strata.justbooksclc.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.DBHelper;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.SharedValue;
import com.strata.justbooksclc.activity.SingleMenuItemActivity;
import com.strata.justbooksclc.adapter.WishlistAdapter;
import com.strata.justbooksclc.model.Book;
import com.strata.justbooksclc.tabs.TabLayoutActivity;

public class WishlistFragment extends Fragment {
	//private static final String SERVER_BASE_URL = "192.168.2.113:4321";
	// JSON Node names
	private static final String TAG_WISHLIST = "wishlists";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	private static final String RENTAL_ID = "rental_id";
	private static final String TAG_LANGUAGE = "language"; 
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "title_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	//private JSONParse json_parse = new JSONParse();
	private JSONParse json_parse;
	
	String auth_token;
	String memb;
	String numb;
	WishlistAdapter adapter;
	boolean calculated = false;
    int numberOfColumns = 0;
    int numberOfRows = 0;
    int heightInPixels = 0;
    ProgressBar progress;
    RelativeLayout empty_shelf_dialog;
	Button btn_lets_go;
	Button btn_later;
	GridView gridview;
	// contacts JSONArray
	JSONArray list = null;
	private DBHelper mydb;
	ArrayList<Book> bookList = new ArrayList<Book>();
	int lastClickedPosition = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.wishlist_layout, container, false);
		gridview = (GridView) rootView.findViewById(R.id.gridview);
		progress = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		empty_shelf_dialog = (RelativeLayout)rootView.findViewById(R.id.empty_shelf_dialog);
		btn_lets_go = (Button)rootView.findViewById(R.id.btn_lets_go);
		btn_later = (Button)rootView.findViewById(R.id.btn_later);
		
		return rootView;
		
	}

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    
    mydb = new DBHelper(getActivity());
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	
	getDimens();
	SharedPreferences value = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
	auth_token = value.getString("AUTH_TOKEN","");
	memb = value.getString("MEMBERSHIP_NO","");
	numb = value.getString("NUMBER","");
	
	UpdateListFromDatabase(3);
	//json_parse = new JSONParse();
	//json_parse.execute();
	
	gridview.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			if(bookList.size() > position){
				lastClickedPosition = position;
				Intent in = new Intent(getActivity().getApplicationContext(),SingleMenuItemActivity.class);
				in.putExtra(TAG_AUTHOR, bookList.get(position).getAuthor());
	            in.putExtra(TAG_CATEGORY, bookList.get(position).getCategory());
	            in.putExtra(TAG_TITLE, bookList.get(position).getTitle());
	            in.putExtra(TAG_LANGUAGE, bookList.get(position).getPublisher());
	            in.putExtra(TAG_PAGE, bookList.get(position).getPrice());
	            in.putExtra(TAG_IMAGE_URL, bookList.get(position).getImage_url());
	            in.putExtra(SUMMARY, bookList.get(position).getSummary());
	            in.putExtra(RENTAL_ID, "");
	            in.putExtra(TAG_ID, bookList.get(position).getId());
	            in.putExtra(TIMES_RENTED, bookList.get(position).getTimes_rented());
	            in.putExtra(AVG_READING, bookList.get(position).getAvg_reading());
				in.putExtra("message", "destroy");
				in.putExtra("check","logged_in");
				startActivity(in);
			}else if(bookList.size() == position){
				Intent in = new Intent(getActivity(), TabLayoutActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(in);
			}
			
		}
	});
	btn_lets_go.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent in = new Intent(getActivity(), TabLayoutActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(in);
			empty_shelf_dialog.setVisibility(View.GONE);
		}
	});
	
	btn_later.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			empty_shelf_dialog.setVisibility(View.GONE);
		}
	});
  }

  private class JSONParse extends AsyncTask<String,String,JSONObject>{
	  protected void onPreExecute(){
		  
	  }
	  protected JSONObject doInBackground(String... args){
		  String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/wishlists.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
		  JSONParser jp = new JSONParser();
		  JSONObject json = jp.getJSONFromUrl(url);
		  if (isCancelled()){
          	  return null;}
          else
			return json;
	  }
	  protected void onPostExecute(JSONObject json){
		progress.setVisibility(View.GONE);
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
					mydb.insertAllData(bookArrayStringed,3);
					adapter.notifyDataSetChanged();
					if(list.length() == 0)
						empty_shelf_dialog.setVisibility(View.VISIBLE);
				}else{
					//setEmptyText("No books in wishlist");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				//setEmptyText("No books in wishlist");
			}
			
			//adapter = new WishlistAdapter(getActivity(), bookList,numberOfRows,numberOfColumns,heightInPixels);
			// selecting single ListView item
		    //gridview.setAdapter(adapter);
			}
	  }
  }
  
  protected void getDimens() {
	  DisplayMetrics displayMetrics = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
      float height = (float) Math.floor(displayMetrics.heightPixels/displayMetrics.density);
      float width = (float) Math.floor(displayMetrics.widthPixels/displayMetrics.density);
      //float a = height/185; 
      numberOfRows = (int) Math.floor(width/120);
      numberOfColumns = (int) Math.floor(height/185);
      heightInPixels = (int) Math.ceil(185*displayMetrics.density);
  }
  
    private void UpdateListFromDatabase(final int slot){
	    new Thread(new Runnable() {
	        public void run() {
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
	    			progress.setVisibility(View.GONE);
	    			System.out.println("wish_list_is_not_empty");
	    		}
	    		adapter = new WishlistAdapter(getActivity(), bookList,numberOfRows,numberOfColumns,heightInPixels);
	
	        	gridview.post(new Runnable() {
	                public void run() {
	                    gridview.setAdapter(adapter);
	                }
	            });
	        }
        }).start();
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
		if((SharedValue.data().hasBeenChanged && adapter != null) || bookList.isEmpty()){
			if(lastClickedPosition >= 0){
				bookList.remove(lastClickedPosition);
				adapter.notifyDataSetChanged();
			}
			json_parse = new JSONParse();
			json_parse.execute();
		}
		SharedValue.data().hasBeenChanged = false;
		lastClickedPosition = -1;
  }
  public void onDestroy(){
	  super.onDestroy();
	  if(json_parse!=null)
		  json_parse.cancel(true);
	  mydb.close();
  }

} 