package com.example.myfirstapp;

public interface Config {

	// used to share GCM regId with application server - using php app server
	//static final String APP_SERVER_URL = "http://staging.justbooksclc.com:8787/api/v1/push_notifications/receive";
	static final String APP_SERVER_URL = "http://192.168.2.109:3000/api/v1/push_notifications/receive";
	
	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "959617453751";
	static final String MESSAGE_KEY = "message";
	static final String NOTIFICATION_ID = "notification_id";
	static final String SERVER_BASE_URL = "staging.justbooksclc.com:8787";
}