package com.strata.justbooksclc.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.newrelic.agent.android.NewRelic;
import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.JSONParser;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.adapter.ReviewAdapter;
import com.strata.justbooksclc.model.Review;

public class AllReviewsActivity extends ListActivity{
	String rest_id;
	String review_text;
	EditText my_review;
	JSONParse json_parse;
	JSONArray list = null;
	ReviewAdapter adapter;
	ArrayList<Review> review_list;
	Context context;
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    int itemId = item.getItemId();
	    if (itemId == android.R.id.home){
	    	finish();
	    	return true;
	    }else {
	    	return super.onOptionsItemSelected(item);
	    }
	  }
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//settingup new relic
		NewRelic.withApplicationToken("AA6bdf42b2e97af26de101413a456782897ba273f7").start(this.getApplication());
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String my_theme = value.getString("MY_THEME", "");
			
		if (my_theme.equals("green"))
			setTheme(R.style.MyThemeGreen);
		else if (my_theme.equals("brown"))
			setTheme(R.style.MyThemeBrown);
		else if (my_theme.equals("violet"))
			setTheme(R.style.MyThemeViolet);
		else if (my_theme.equals("blue"))
			setTheme(R.style.MyThemeBlue);
		else
			setTheme(R.style.MyTheme);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		rest_id = getIntent().getStringExtra("rest_id");
		context = getApplicationContext();
		setTitle(getIntent().getStringExtra("biz_name"));

		json_parse = new JSONParse();
		String url = "http://" + Config.SERVER_BASE_URL+"/api/v1/title/get_all_reviews.json";
		json_parse.execute(url);
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {

		protected JSONObject doInBackground(String... args) {
			JSONParser jp = new JSONParser();
			JSONObject json = jp.getJSONFromUrl(args[0]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			if (json!=null && !isCancelled()) {
				try{
					JSONObject organization = json.getJSONObject("organization");
					list = organization.getJSONArray("reviews");
					review_list = new ArrayList<Review>();

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							JSONObject c = list.getJSONObject(i);
							Review review = new Review();
							review.setReview_text(c.getString("content"));
							review.setPublisher_name(c.getString("name"));
							review_list.add(review);
						}
					}
					adapter = new ReviewAdapter(context, review_list);
					// selecting single ListView item
					setListAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				catch (JSONException e) {
					e.printStackTrace();
					Toast toast = Toast.makeText(getApplicationContext(),
							"Failed to post your review",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 170);
					toast.show();
				}
			}
		}
	}
}

