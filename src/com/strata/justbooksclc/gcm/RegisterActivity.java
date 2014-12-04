package com.strata.justbooksclc.gcm;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.strata.justbooksclc.Config;

public class RegisterActivity {

	Button btnGCMRegister;
	Button btnAppShare;
	GoogleCloudMessaging gcm;
	Context context;
	String regId;
	ShareExternalServer appUtil;
	AsyncTask<Void, Void, String> shareRegidTask;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";


	public void start_registration(Context this_context) {
		context = this_context;

		registerGCM();
		//Log.d("RegisterActivity", "GCM RegId: " + regId);
	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(context);
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			//Log.d("RegisterActivity","registerGCM - successfully registered with GCM server - regId: "+ regId);
		} else {
			//Toast.makeText(context,"RegId already available. RegId: " + regId,Toast.LENGTH_SHORT).show();
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences pref = context.getSharedPreferences("PREF",Context.MODE_PRIVATE);
		String registrationId = pref.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			//Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = pref.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			//Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			//Log.d("RegisterActivity","I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					//Log.d("RegisterActivity", "registerInBackground - regId: "+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					//Log.d("RegisterActivity", "Error: " + msg);
				}
				//Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				//Toast.makeText(context,"Registered with GCM Server." + msg, Toast.LENGTH_SHORT).show();
				if (TextUtils.isEmpty(regId)) {
					Toast.makeText(context, "GCM Registration failed!",Toast.LENGTH_SHORT).show();
				} else {
					appUtil = new ShareExternalServer();
					//Log.d("GCMMainActivity", "regId: " + regId);

					shareRegidTask = new AsyncTask<Void, Void, String>() {
						@Override
						protected String doInBackground(Void... params) {
							String result = appUtil.shareRegIdWithAppServer(context, regId);
							return result;
						}

						@Override
						protected void onPostExecute(String result) {
							shareRegidTask = null;
							//Toast.makeText(context, result,Toast.LENGTH_SHORT).show();
						}

					};
					shareRegidTask.execute(null, null, null);
				}
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences pref = context.getSharedPreferences("PREF",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		int appVersion = getAppVersion(context);
		//Log.i(TAG, "Saving regId on app version " + appVersion);
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}
}
