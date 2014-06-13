package com.example.myfirstapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FrontPage extends FragmentActivity {
	private ProgressDialog progress;
	@SuppressWarnings("unused")
	private Context mContext;
	//private static final String SERVER_BASE_URL = "192.168.2.113:4321";
	// JSON Node names
	private static final String TAG_WISHLIST = "titles";
//	private static final String TAG_AUTHOR = "author";
//	private static final String TAG_CATEGORY = "category";
//	private static final String TAG_PAGE = "no_of_pages";
//	private static final String TAG_LANGUAGE = "language";
//	private static final String TAG_TITLE = "title";
	private static final String TAG_ISBN = "isbn";
	private static final String TAG_ID = "id";
	final String[][] myarray = new String[10][10];
	JSONArray list = null;

	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.front_page, menu);
        menuInflater.inflate(R.menu.activity_main_actions, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
	  }
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		progress = new ProgressDialog(this);
		progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
		int itemId = item.getItemId();
		if (itemId == R.id.action_back) {
			// Single menu item is selected do something
            // Ex: launching new activity/screen or show alert message
            finish();
			return true;
		} else if (itemId == R.id.action_search) {
			Intent searchlib = new Intent(getApplicationContext(), SearchPage.class);
    		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_storage) {
			Intent searchlib = new Intent(getApplicationContext(), AndroidTabLayoutActivity.class);
    		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_place) {
			Intent searchlib = new Intent(getApplicationContext(), MyMap.class);
    		startActivity(searchlib);
			return true;
		} else if (itemId == R.id.action_help) {
			Intent about = new Intent(getApplicationContext(), HelpActivity.class);
    		startActivity(about);
			return true;
		} else if (itemId == R.id.logout) {
			SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		    SharedPreferences.Editor   editor = preferences.edit();
		    editor.putString("AUTH_TOKEN", "0");
		    editor.putString("MEMBERSHIP_NO", "0");
		    editor.putString("DATE_OF_SIGNUP", "0");
		    editor.commit();
		    progress.setMessage("Loading");
	        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        progress.setIndeterminate(true);
	        progress.show();
	        
		    Intent login = new Intent(getApplicationContext(), SignupPage.class);
		    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		startActivity(login);
			return true;
		}else {
			return super.onOptionsItemSelected(item);
		}
	  }
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.front_page);
        progress = new ProgressDialog(this);
        progress.hide();

        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

    }
	private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {

        	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy);

        	SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        	String auth_token = value.getString("AUTH_TOKEN","");
        	String memb = value.getString("MEMBERSHIP_NO","");
        	String numb = value.getString("NUMBER","");

        	System.out.println("score");
        	String url = "http://"+Config.SERVER_BASE_URL+"/api/v1/top_rentals.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
        	// Creating JSON Parser instance
        	JSONParser jParser = new JSONParser();
        	// getting JSON string from URL
        	JSONObject json = jParser.getJSONFromUrl(url);
        	try {
        		// Getting Array of Contacts
        		list = json.getJSONArray(TAG_WISHLIST);

        		// looping through All Contacts
        		for (int i = 0; i < 7; i++) {
        			JSONObject c = list.getJSONObject(i);

        			// Storing each json item in variable
//        			myarray[i][0] = c.getString(TAG_AUTHOR);
//        			myarray[i][1] = c.getString(TAG_CATEGORY);
//        			myarray[i][2] = c.getString(TAG_PAGE);
//        			myarray[i][3] = c.getString(TAG_LANGUAGE);
//        			myarray[i][4] = c.getString(TAG_TITLE);
        			myarray[i][0] = c.getString(TAG_ISBN);
        			myarray[i][1] = c.getString(TAG_ID);

        		}
        	} catch (JSONException e) {
        		e.printStackTrace();
        	}
        	return FirstFragment.newInstance("http://cdn2.justbooksclc.com/medium/"+myarray[pos+1][0]+".jpg",myarray[pos+1][1]);
			/*switch(pos) {
            case 0: return FirstFragment.newInstance(myarray[1][0],myarray[1][1],myarray[1][2],myarray[1][3],myarray[1][4],"http://cdn2.justbooksclc.com/medium/"+myarray[1][5]+".jpg",myarray[1][6]);
            case 1: return FirstFragment.newInstance(myarray[2][0],myarray[2][1],myarray[2][2],myarray[2][3],myarray[2][4],"http://cdn2.justbooksclc.com/medium/"+myarray[2][5]+".jpg",myarray[2][6]);
            case 2: return FirstFragment.newInstance(myarray[3][0],myarray[3][1],myarray[3][2],myarray[3][3],myarray[3][4],"http://cdn2.justbooksclc.com/medium/"+myarray[3][5]+".jpg",myarray[3][6]);
            case 3: return FirstFragment.newInstance(myarray[4][0],myarray[4][1],myarray[4][2],myarray[4][3],myarray[4][4],"http://cdn2.justbooksclc.com/medium/"+myarray[4][5]+".jpg",myarray[4][6]);
            case 4: return FirstFragment.newInstance(myarray[5][0],myarray[5][1],myarray[5][2],myarray[5][3],myarray[5][4],"http://cdn2.justbooksclc.com/medium/"+myarray[5][5]+".jpg",myarray[5][6]);
            case 5: return FirstFragment.newInstance(myarray[6][0],myarray[6][1],myarray[6][2],myarray[6][3],myarray[6][4],"http://cdn2.justbooksclc.com/medium/"+myarray[6][5]+".jpg",myarray[6][6]);
            case 6: return FirstFragment.newInstance(myarray[7][0],myarray[7][1],myarray[7][2],myarray[7][3],myarray[7][4],"http://cdn2.justbooksclc.com/medium/"+myarray[7][5]+".jpg",myarray[7][6]);  
            default: return FirstFragment.newInstance("a","b","c","d","e","f","g");
            }*/

        }

        @Override
        public int getCount() {
            return 6;
        }
    }

	@Override
	public void onResume(){
		super.onResume();
		progress.hide();
	}
    @Override
    public void onBackPressed()
    {
    	finish();
    }

}
