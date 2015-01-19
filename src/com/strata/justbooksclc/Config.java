package com.strata.justbooksclc;

public interface Config {
	static final String APP_SERVER_URL = "http://mapi.justbooksclc.com/api/v1/push_notifications/receive";
	//static final String APP_SERVER_URL = "http://staging.justbooksclc.com:8787/api/v2/push_notifications/receive";
	//static final String APP_SERVER_URL = "http://192.168.1.4:4000/api/v2/push_notifications/receive";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "959617453751";
	//static final String GOOGLE_PROJECT_ID = "axial-keep-585";
	static final String MESSAGE_KEY = "message";
	static final String NOTIFICATION_ID = "notification_id";
	//static final String SERVER_BASE_URL = "staging.justbooksclc.com:8787/api/v2";
	//static final String SERVER_BASE_URL = "192.168.1.4:4000/api/v2";
	static final String SERVER_BASE_URL = "mapi.justbooksclc.com/api/v1";
}