package com.strata.justbooksclc;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TopRentalFragment extends ListFragment {
	private ProgressDialog progress;
	// JSON Node names
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String SUMMARY = "summary";
	private static final String RENTAL_ID = "rental_id";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID_call = "title_id";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
	private DBHelper mydb;
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    progress = new ProgressDialog(this.getActivity());
    
    ColorDrawable gray = new ColorDrawable(this.getResources().getColor(R.color.gray));
	getListView().setDivider(gray);
	getListView().setDividerHeight(1);
	
	mydb = new DBHelper(getActivity());
	ArrayList<String> db_list = new ArrayList<String>();
	db_list = mydb.getAllCotacts(2);
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
		in.putExtra(RENTAL_ID, "");
		in.putExtra(TAG_ID_call, title_id);
		in.putExtra(TIMES_RENTED,times_rented);
		in.putExtra(AVG_READING,avg_reading);
		in.putExtra("message", "create");
		in.putExtra("check","logged_in");
		
		startActivity(in);
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
	  mydb.close();
    }
 } 