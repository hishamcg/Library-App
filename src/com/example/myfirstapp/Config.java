package com.example.myfirstapp;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://192.168.2.117:4321/api/v1/push_notifications/receive";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "1091968141851";
	static final String MESSAGE_KEY = "message";
	static final String NOTIFICATION_ID = "notification_id";

}