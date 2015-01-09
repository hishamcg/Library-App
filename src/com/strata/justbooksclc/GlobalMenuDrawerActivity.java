package com.strata.justbooksclc;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.strata.justbooksclc.activity.BookBand;
import com.strata.justbooksclc.activity.FindBookActivity;
import com.strata.justbooksclc.activity.HelpActivity;
import com.strata.justbooksclc.activity.PageZero;
import com.strata.justbooksclc.activity.Policy;
import com.strata.justbooksclc.activity.ProfilePage;
import com.strata.justbooksclc.adapter.NavDrawerListAdapter;
import com.strata.justbooksclc.map.MyMap;
import com.strata.justbooksclc.model.NavDrawerItem;
import com.strata.justbooksclc.signin.SigninActivity;
import com.strata.justbooksclc.signin.Signup;
import com.strata.justbooksclc.tabs.TabLayoutActivity;
import com.strata.justbooksclc.tabs.TabMyListActivity;

public class GlobalMenuDrawerActivity extends FragmentActivity{
    protected FrameLayout actContent;
    //for drawer
  	private DrawerLayout mDrawerLayout;
  	private ListView mDrawerList;
  	private LinearLayout mDrawerLinear;
  	private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
  	private CharSequence mDrawerTitle;
    // used to store app title
  	private CharSequence mTitle;
    // slide menu items
  	private String[] navMenuTitles;
  	private TypedArray navMenuIcons;
  	private TypedArray menuThemeIcons;
  	private ArrayList<NavDrawerItem> navDrawerItems;
  	private NavDrawerListAdapter adapter;
  	private String numb = "";
  	String my_theme = "gray";
  	int sl = 0;
  	String email;
    
    @Override
    public void setContentView(final int layoutResID) {
    	mDrawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.global_drawer_menu_layout, null); // Your base layout here
        actContent= (FrameLayout) mDrawerLayout.findViewById(R.id.frame_container);
        getLayoutInflater().inflate(layoutResID, actContent, true); // Setting the content of layout your provided to the act_content frame
        super.setContentView(mDrawerLayout);
        setUpDrawer();
        // here you can get your drawer buttons and define how they should behave and what must they do, so you won't be needing to repeat it in every activity class
    }
    
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  MenuInflater menuInflater = getMenuInflater();
	      menuInflater.inflate(R.menu.main, menu);
	      MenuItem overflow = menu.findItem(R.id.action_overflow);
	      MenuItem user = menu.findItem(R.id.action_user);
	      MenuItem action_level = menu.findItem(R.id.action_level);
	      MenuItem action_logout = menu.findItem(R.id.action_logout);
	      
	      SpannableString s = new SpannableString("Logout");
	      s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
	      action_logout.setTitle(s);
	      
	      user.setTitle(email);
	      //changing bookband heart color according to the theme
	      setJustBooksTheme(action_level);
	      //locking the view for bookband
	      menulockBookband(menu);
	      
	      if (numb != null && numb != "") {           
	    	  overflow.setVisible(true);
	      }else{
	    	  overflow.setVisible(false);
	      }
	      SearchManager searchManager =
	                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	      SearchView searchView =
	                 (SearchView) menu.findItem(R.id.action_search).getActionView();
	      searchView.setSearchableInfo(
	                 searchManager.getSearchableInfo(getComponentName()));
	      //searchView.setOnQueryTextListener(this);
	      //return true;
	      int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	      TextView textView = (TextView) searchView.findViewById(id);
	      textView.setHintTextColor(0x88ffffff);
	      textView.setTextColor(0xffffffff);
	      return super.onCreateOptionsMenu(menu);
	    }
	private void setJustBooksTheme(MenuItem action_level){
		if(my_theme.equals("green"))
	    	  action_level.setIcon(R.drawable.ic_heart_green);
	      else if(my_theme.equals("brown"))
	    	  action_level.setIcon(R.drawable.ic_heart_brown);
	      else if(my_theme.equals("violet"))
	    	  action_level.setIcon(R.drawable.ic_heart_violet);
	      else if(my_theme.equals("blue"))
	    	  action_level.setIcon(R.drawable.ic_heart_blue);
	      else
	    	  action_level.setIcon(R.drawable.ic_heart_gray);
	}
	private void menulockBookband(Menu menu){
		//locking the view for bookband
	      MenuItem[] menu_array = {menu.findItem(R.id.action_newbie),
					    		  menu.findItem(R.id.action_rookie),
					    		  menu.findItem(R.id.action_pro),
					    		  menu.findItem(R.id.action_guru)};
	      for(int i=3;i>=sl;i--){
	    	  menu_array[i].setIcon(R.drawable.ic_lock);
	      }

	}
	@SuppressWarnings("deprecation")
	private void themeChange(int customer_level){
		menuThemeIcons = getResources().obtainTypedArray(R.array.themes_icon);
    		
		if (sl >= customer_level){
			SharedPreferences preferences = getSharedPreferences("PREF", Context.MODE_PRIVATE);
	        SharedPreferences.Editor   editor = preferences.edit();
	        editor.putString("MY_THEME", getResources().getStringArray(R.array.justbooks_themes_array)[customer_level]);
		    editor.commit();
			finish();
		    Intent in = new Intent(getApplicationContext(), TabMyListActivity.class);
		    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(in);
	   }else {
		final AlertDialog alert = new AlertDialog.Builder(this).create();
	        alert.setTitle("Book Band");
	        alert.setMessage(getResources().getStringArray(R.array.themes_locked_message)[customer_level]);
	        alert.setButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
	        	   alert.cancel();
	           }
	        });
	        alert.setIcon(menuThemeIcons.getResourceId(customer_level, -1));
	        alert.show();
	        menuThemeIcons.recycle();
	}
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	  	
	    if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
	    mDrawerLayout.closeDrawer(mDrawerLinear);
	    int itemId = item.getItemId();
	    if (itemId == R.id.action_search) {
	      return true;
	    }else if (itemId == R.id.action_policy ){
	    	Intent policy = new Intent(getApplicationContext(), Policy.class);
	        startActivity(policy);
	    	return true;
	    }else if (itemId == R.id.action_logout ){
	    	SharedPreferences pref = getSharedPreferences("PREF",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString("AUTH_TOKEN", "");
		    editor.putString("MEMBERSHIP_NO", "");
		    editor.putString("DATE_OF_SIGNUP", "");
		    editor.putString("NUMBER", "");
		    editor.putInt("BOOK_BAND", 0);
		    editor.putString("MY_THEME", "");
		    editor.commit();
		    finish();
		    Intent logout = new Intent(getApplicationContext(),PageZero.class);
		    logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    startActivity(logout);
	    	return true;
	    }else if (itemId == R.id.action_address ){
	    	Intent profile = new Intent(getApplicationContext(), ProfilePage.class);
	        startActivity(profile);
	    	return true;
	    }else if (itemId == R.id.action_help_me){
	    	Intent bookband = new Intent(getApplicationContext(), BookBand.class);
	        startActivity(bookband);
	    	return true;
	    }else if (itemId == R.id.action_dull && !my_theme.equals("gray")){
	    	themeChange(0);
	    	return true;
	    }else if (itemId == R.id.action_newbie && !my_theme.equals("brown")){
	    	themeChange(1);
	    	return true;
	    }else if (itemId == R.id.action_rookie && !my_theme.equals("green")){
	    	themeChange(2);
	    	return true;
	    }else if (itemId == R.id.action_pro && !my_theme.equals("violet")){
	    	themeChange(3);
	    	return true;
	    }else if (itemId == R.id.action_guru && !my_theme.equals("blue")){
	    	themeChange(4);
	    	return true;
	    }
	    else {
	      return super.onOptionsItemSelected(item);
	    }
    }
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setting up new relic
		InitializeCustomData();
	}
	
	private void InitializeCustomData(){
		SharedPreferences value = getSharedPreferences("PREF", Context.MODE_PRIVATE);
		numb = value.getString("NUMBER","");
		my_theme = value.getString("MY_THEME", "");
		if (numb != null && numb != ""){
			sl = value.getInt("BOOK_BAND",0);
			email = value.getString("EMAIL", "guest@email.com");}
			
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
	}
	
	
	private void setUpDrawer(){
		//drawer
		mTitle = getTitle();
		mDrawerTitle = "Menu";
		if (numb != null && numb != ""){
			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		}else{
			// load slide menu items
			navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items_non_user);
			// nav drawer icons from resources
			navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons_non_user);
		}
		
		//mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tab);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		mDrawerLinear = (LinearLayout) findViewById(R.id.linear_slide);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		
		for (int i=0;i < navMenuTitles.length;i++){
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
		}
		
		// Recycle the typed array
		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);
		// enabling action bar app icon and behaving it as toggle button
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer_white, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	private class SlideMenuClickListener implements ListView.OnItemClickListener {
  		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// display view for selected nav drawer item
  			if (numb != null && numb != ""){
  				displayView(position);
  			}else{
  				displayViewNonUser(position);
  			}
		}
  	}
  	private void displayView(int position) {
		// update the main content by replacing fragments
		//Fragment fragment = null;
		switch (position) {
		case 0:
			Intent tab = new Intent(getApplicationContext(), TabMyListActivity.class);
			tab.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(tab);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
			Intent fin = new Intent(getApplicationContext(), FindBookActivity.class);
			fin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(fin);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 2:
			Intent lay = new Intent(getApplicationContext(), TabLayoutActivity.class);
			lay.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(lay);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 3:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from", "front_page");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerLinear);
	  		break;
		case 4:
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;

		default:
			break;
		}

	}
  	private void displayViewNonUser(int position) {
		switch (position) {
		case 0:
			Intent fin = new Intent(getApplicationContext(), FindBookActivity.class);
			fin.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        startActivity(fin);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 1:
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 2:
			Intent location = new Intent(getApplicationContext(), MyMap.class);
			location.putExtra("from", "front_page");
	  		startActivity(location);
	  		mDrawerLayout.closeDrawer(mDrawerLinear);
	  		break;
		case 3:
			Intent help = new Intent(getApplicationContext(),HelpActivity.class);
			startActivity(help);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 4:
			Intent searchlib = new Intent(getApplicationContext(), SigninActivity.class);
    		startActivity(searchlib);
			mDrawerLayout.closeDrawer(mDrawerLinear);
			break;
		case 5:
			Intent sign_up_call = new Intent(getApplicationContext(), Signup.class);
    		startActivity(sign_up_call);
		    mDrawerLayout.closeDrawer(mDrawerLinear);
			break;

		default:
			break;
		}
	}
  	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
  	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	

}
