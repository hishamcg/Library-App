/*package com.example.myfirstapp;
 
import android.app.Activity;
import android.os.Bundle;
 
public class BestsellerList extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bestseller_layout);
    }
}*/

package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class BestsellerList extends Activity {

	// url to make request
	private static final String SEARCH_URL = "url";
	private static final String SERVER_BASE_URL = "192.168.2.118:4321";
	// JSON Node names
	private static final String TAG_WISHLIST = "wishlists";
	private static final String TAG_AUTHOR = "author_id";
	private static final String TAG_IMAGE_URL = "image";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ISBN = "isbn";

	// contacts JSONArray
	JSONArray list = null;

/*	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bestseller_layout);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		SharedPreferences value = getPreferences(MODE_PRIVATE);
		String auth_token = value.getString("AUTH_TOKEN","");
		
		System.out.println("score");
		String url = "http://"+SERVER_BASE_URL+"/api/v1/wishlists.json?auth_token=SRciNGKqPKDqEStase-9&phone=9686448557";
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
				String page = c.getString(TAG_PAGE);
				String language = c.getString(TAG_LANGUAGE);
				String title = c.getString(TAG_TITLE);
				String isbn = c.getString(TAG_ISBN);
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
				book.setPrice(page);
				book.setPublisher(language);
				//book.setImageUrl("http://"+SERVER_BASE_URL+"/assets/"+imageUrl);
				book.setImageUrl("http://cdn2.justbooksclc.com/medium/"+isbn+".jpg");
				book.setIsbn(isbn);
				
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
				String isbn = ((TextView) view.findViewById(R.id.isbn))
						.getText().toString();
				System.out.println("########isbn#######"+isbn);
				System.out.println("#######title#######"+title);
				System.out.println("########publisher#######"+publisher);
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						SingleMenuItemActivity.class);
				in.putExtra(TAG_AUTHOR, author);
				in.putExtra(TAG_TITLE, title);
				in.putExtra(TAG_LANGUAGE, publisher);
				in.putExtra(TAG_PAGE, price);
				in.putExtra(TAG_IMAGE_URL, "http://cdn2.justbooksclc.com/medium/"+isbn+".jpg");
				startActivity(in);

			}
		});

	}

}
