package com.example.myfirstapp;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterFragment extends Fragment {

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	return inflater.inflate(R.layout.gcm_activity_register, container,false);
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getView();
		context = getActivity();

		btnGCMRegister = (Button) view.findViewById(R.id.btnGCMRegister);
		btnGCMRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(regId)) {
					regId = registerGCM();
					Log.d("RegisterActivity", "GCM RegId: " + regId);
				} else {
					regId = registerGCM();
					Toast.makeText(getActivity(),
							"Already Registered with GCM Server!",
							Toast.LENGTH_LONG).show();
				}
				if (TextUtils.isEmpty(regId)) {
					Toast.makeText(getActivity(), "RegId is empty!",
							Toast.LENGTH_LONG).show();
				} else {
					appUtil = new ShareExternalServer();
					Log.d("GCMMainActivity", "regId: " + regId);

					shareRegidTask = new AsyncTask<Void, Void, String>() {
						@Override
						protected String doInBackground(Void... params) {
							String result = appUtil.shareRegIdWithAppServer(context, regId);
							return result;
						}

						@Override
						protected void onPostExecute(String result) {
							shareRegidTask = null;
							Toast.makeText(getActivity(), result,
									Toast.LENGTH_LONG).show();
						}

					};
					shareRegidTask.execute(null, null, null);
					getActivity().finish();
				}
				
			}
		});

//		btnAppShare = (Button) findViewById(R.id.btnAppShare);
//		btnAppShare.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if (TextUtils.isEmpty(regId)) {
//					Toast.makeText(getApplicationContext(), "RegId is empty!",
//							Toast.LENGTH_LONG).show();
//				} else {
//					appUtil = new ShareExternalServer();
//
//					regId = getIntent().getStringExtra("regId");
//					Log.d("GCMMainActivity", "regId: " + regId);
//
//					shareRegidTask = new AsyncTask<Void, Void, String>() {
//						@Override
//						protected String doInBackground(Void... params) {
//							String result = appUtil.shareRegIdWithAppServer(context, regId);
//							return result;
//						}
//
//						@Override
//						protected void onPostExecute(String result) {
//							shareRegidTask = null;
//							Toast.makeText(getApplicationContext(), result,
//									Toast.LENGTH_LONG).show();
//						}
//
//					};
//					shareRegidTask.execute(null, null, null);
////					Intent i = new Intent(getApplicationContext(),
////							GCMMainActivity.class);
////					i.putExtra("regId", regId);
////					Log.d("RegisterActivity",
////							"onClick of Share: Before starting main activity.");
////					startActivity(i);
//					finish();
//				}
//			}
//		});
	}

	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(getActivity());
		regId = getRegistrationId(context);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			Toast.makeText(getActivity(),
					"RegId already available. RegId: " + regId,
					Toast.LENGTH_LONG).show();
		}
		return regId;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getActivity().getSharedPreferences(
				GCMMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
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
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
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
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Toast.makeText(getActivity(),
						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
						.show();
			}
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getActivity().getSharedPreferences(
				GCMMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}
}
