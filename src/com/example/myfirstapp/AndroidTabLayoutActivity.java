package com.example.myfirstapp;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
 
@SuppressWarnings("deprecation")
public class AndroidTabLayoutActivity extends TabActivity {
	private ProgressDialog progress;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        setContentView(R.layout.main);
		
		TabHost tabHost = getTabHost();
         
        // Tab for Photos
        TabSpec bestsellerspec = tabHost.newTabSpec("");
        // setting Title and Icon for the Tab
        
        bestsellerspec.setIndicator("wish list", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent photosIntent = new Intent(this, BestsellerList.class);
        bestsellerspec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec latestspec = tabHost.newTabSpec("");        
        latestspec.setIndicator("top rated", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent songsIntent = new Intent(this, LatestList.class);
        latestspec.setContent(songsIntent);
         
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(bestsellerspec); // Adding photos tab
        tabHost.addTab(latestspec); // Adding songs tab
        progress.hide();
    }
}
