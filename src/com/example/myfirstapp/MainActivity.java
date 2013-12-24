package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	// url to make request
	// private static String url =
	// "http://192.168.20.101:3000/mylib/search.json?q=a";
	private static final String SEARCH_URL = "url";
	//private static final String SERVER_BASE_URL = "192.168.1.12:3000";
	private static final String SERVER_BASE_URL = "dry-everglades-8791.herokuapp.com";
	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY_ID = "cat_id";
	private static final String TAG_CREATED_AT = "created_at";
	private static final String TAG_EDITION = "edition";
	private static final String TAG_ID = "id";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_PRICE = "price";
	private static final String TAG_PUBLISHER = "publisher";
	private static final String TAG_TITLE = "title";
	private static final String TAG_UPDATED_AT = "updated_at";

	// private static final String TAG_PHONE = "phone";
	// private static final String TAG_PHONE_MOBILE = "mobile";
	// private static final String TAG_PHONE_HOME = "home";
	// private static final String TAG_PHONE_OFFICE = "office";

	// contacts JSONArray
	JSONArray contacts = null;

	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		System.out.println("score");
		Intent searchlib = getIntent();
		String fin = searchlib.getStringExtra(SEARCH_URL);

		String url = "http://"+SERVER_BASE_URL+"/mylib/search.json?q=" + fin;

		// Hashmap for ListView
		List<Book> bookList = new ArrayList<Book>();

		// Creating JSON Parser instance
		JSONParser jParser = new JSONParser();

		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			// Getting Array of Contacts
			contacts = json.getJSONArray(TAG_CONTACTS);

			// looping through All Contacts
			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);

				// Storing each json item in variable
				String author = c.getString(TAG_AUTHOR);
				String id = c.getString(TAG_ID);
				String imageUrl = c.getString(TAG_IMAGE_URL);
				String price = "Price: Rs"+c.getString(TAG_PRICE);
				String publisher = c.getString(TAG_PUBLISHER);
				String title = c.getString(TAG_TITLE);

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
				book.setPrice(price);
				book.setPublisher(publisher);
				//book.setImageUrl("http://"+SERVER_BASE_URL+"/assets/"+imageUrl);
				book.setImageUrl(imageUrl);
				
				
				// adding HashList to ArrayList
				bookList.add(book);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/**
		 * Updating parsed JSON data into ListView
		 * */
		/*ListAdapter adapter = new SimpleAdapter(this, contactList,
				R.layout.list_item, new String[] { TAG_AUTHOR, TAG_PUBLISHER,
						TAG_PRICE, TAG_TITLE, TAG_IMAGE }, new int[] {
						R.id.author, R.id.publisher, R.id.price, R.id.title,
						R.id.image });*/
		// ImageView imageView = (ImageView) ArrayList.findViewById(R.id.image);
		// imageView.setImageResource(R.drawable.image);

		// adapter.setImageResource(R.drawable.image);
		Book[] bookAry = new Book[bookList.size()];
		CustomAdapter adapter = new CustomAdapter(this, bookList.toArray(bookAry));
//		setListAdapter(adapter);
		// selecting single ListView item
		ListView lv = (ListView)findViewById(R.id.list);
		lv.setAdapter(adapter);
		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String author = ((TextView) view.findViewById(R.id.author))
						.getText().toString();
				String publisher = ((TextView) view
						.findViewById(R.id.publisher)).getText().toString();
				String price = ((TextView) view.findViewById(R.id.price))
						.getText().toString();
				String title = ((TextView) view.findViewById(R.id.title))
						.getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SingleMenuItemActivity.class);
				in.putExtra(TAG_AUTHOR, author);
				in.putExtra(TAG_TITLE, title);
				in.putExtra(TAG_PRICE, price);
				in.putExtra(TAG_PUBLISHER, publisher);
				startActivity(in);

			}
		});

	}

}
