package com.strata.justbooksclc;

import java.util.HashMap;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
 
public class MyApplication extends Application {
 
/*	// The following line should be changed to include the correct property id.
	private static final String PROPERTY_ID = "UX-XXXXXXXX-X";
	//Logging TAG
	private static final String TAG = "MyApp";*/
	public static int GENERAL_TRACKER = 0;
	 
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	}
	 
	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	public MyApplication() {
		super();
	}
	 
	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			/*Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
			: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker) 
			: analytics.newTracker(PROPERTY_ID);*/
			/*Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
					: analytics.newTracker(R.xml.global_tracker);
			mTrackers.put(trackerId, t);*/
			analytics.setDryRun(false);
			analytics.getLogger().setLogLevel(Logger.LogLevel.INFO);
			Tracker track = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.global_tracker) : null;
            if (track != null) {
            	track.enableAdvertisingIdCollection(true);
            }
			if( trackerId == TrackerName.GLOBAL_TRACKER )
            {
                mTrackers.put(trackerId, track);
            }
		}
		return mTrackers.get(trackerId);
	}
}
