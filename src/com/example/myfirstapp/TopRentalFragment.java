package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TopRentalFragment extends ListFragment {

	private ProgressDialog progress;
	private static final String SERVER_BASE_URL = "staging.justbooksclc.com:8787";
	//private static final String SERVER_BASE_URL = "192.168.2.113:4321";
	// JSON Node names
	private static final String TAG_WISHLIST = "titles";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ISBN = "isbn";
	private static final String TAG_ID = "id";
	private static final String TAG_ID_call = "title_id";

	// contacts JSONArray
	JSONArray list = null;
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    progress = new ProgressDialog(this.getActivity());
	progress.hide();
    
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	
	SharedPreferences value = this.getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
	String auth_token = value.getString("AUTH_TOKEN","");
	String memb = value.getString("MEMBERSHIP_NO","");
	String numb = value.getString("NUMBER","");
	
	System.out.println("score");
	String url = "http://"+SERVER_BASE_URL+"/api/v1/top_rentals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
	// Hashmap for ListView
	List<Book> bookList = new ArrayList<Book>();
	// Creating JSON Parser instance
	JSONParser jParser = new JSONParser();
	// getting JSON string from URL
	JSONObject json = jParser.getJSONFromUrl(url);
	try {
		// Getting Array of Contacts
		list = json.getJSONArray(TAG_WISHLIST);

		// looping through All Contacts
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
			
			System.out.println("########id#######"+title_id);
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
	}
	Book[] bookAry = new Book[bookList.size()];
	CustomAdapter adapter = new CustomAdapter(this.getActivity(), bookList.toArray(bookAry));
//	setListAdapter(adapter);
	// selecting single ListView item
    setListAdapter(adapter);
  }


  public void onListItemClick(ListView l, View view, int position, long id) {
    // do something with the data
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
		System.out.println("########id#######"+title_id);
		System.out.println("#######title#######"+title);
		System.out.println("########publisher#######"+publisher);
		
		// Starting new intent
		Intent in = new Intent(this.getActivity().getApplicationContext(),SingleMenuItemActivity.class);
		in.putExtra(TAG_AUTHOR, author);
		in.putExtra(TAG_CATEGORY, category);
		in.putExtra(TAG_TITLE, title);
		in.putExtra(TAG_LANGUAGE, publisher);
		in.putExtra(TAG_PAGE, price);
		in.putExtra(TAG_IMAGE_URL, "http://cdn2.justbooksclc.com/medium/"+isbn+".jpg");
		in.putExtra(TAG_ID_call, title_id);
		in.putExtra("message", "create");
		in.putExtra("check","logged_in");
		
		startActivity(in);
  }
  public void onResume(){
		super.onResume();
		progress.hide();
	}
} 