package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FirstFragment extends Fragment {
	
	Drawable drawable_from_url(String url, String src_name)
			throws java.net.MalformedURLException, java.io.IOException {
		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(url).getContent()),
				src_name);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
        
        SharedPreferences value = this.getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
    	final String auth_token = value.getString("AUTH_TOKEN","");
    	final String memb = value.getString("MEMBERSHIP_NO","");
    	final String numb = value.getString("NUMBER","");

/*        TextView tv = (TextView) v.findViewById(R.id.author_label);
        tv.setText(getArguments().getString("msg"));
        
        TextView tv2 = (TextView) v.findViewById(R.id.category_label);
        tv2.setText(getArguments().getString("msg2"));
        
        TextView tv3 = (TextView) v.findViewById(R.id.price_label);
        tv3.setText(getArguments().getString("msg3"));
        
        TextView tv4 = (TextView) v.findViewById(R.id.publisher_label);
        tv4.setText(getArguments().getString("msg4"));
        
        TextView tv5 = (TextView) v.findViewById(R.id.title_label);
        tv5.setText(getArguments().getString("msg5"));*/
        
        ImageView tv6 = (ImageView) v.findViewById(R.id.image_label);
        final ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.loading);
        
        Picasso.with(this.getActivity())
        .load(getArguments().getString("msg"))
        .error(R.drawable.ic_launcher)
        .into(tv6, new EmptyCallback() {
            @Override public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }            
            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }            
        });
 
        Picasso.with(this.getActivity())
        .load(getArguments().getString("msg"))
        .into(target);
        
        //tv6.setImageBitmap(getBitmapFromURL(getArguments().getString("msg")));
        
        final LinearLayout rental_btn = (LinearLayout) v.findViewById(R.id.button_rental);
        final LinearLayout click3 = (LinearLayout) v.findViewById(R.id.click3);
        
        
        rental_btn.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/orders/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+getArguments().getString("msg2")+"&membership_no="+memb;
    			
    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);

    	            //httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();
    	            
    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString("info");
	    	            Toast.makeText(getActivity().getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
        	}
        });
        
        click3.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v){
    	        
    	        String url = "http://staging.justbooksclc.com:8787/api/v1/wishlists/create.json?api_key="+auth_token+"&phone="+numb+"&title_id="+getArguments().getString("msg2")+"&membership_no="+memb;
    			
    			InputStream inputStream = null;
    	        String result = "";
    	        try {
    	 
    	            // 1. create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();
    	 
    	            // 2. make POST request to the given URL
    	            HttpPost httpPost = new HttpPost(url);

    	            // 6. httpPost.setHeader("Accept", "");
    	            httpPost.setHeader("Content-type", "");
    	 
    	            // 8. Execute POST request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(httpPost);
    	 
    	            // 9. receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();

    	            // 10. convert inputstream to string
    	            if(inputStream != null)
    	            {//result = convertInputStreamToString(inputStream);
    	            	result = "Worked!";
	    	            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	    	            StringBuilder responseStrBuilder = new StringBuilder();
	
	    	            String inputStr;
	    	            while ((inputStr = streamReader.readLine()) != null)
	    	                responseStrBuilder.append(inputStr);
	
	    	            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
	    	            String INFO = jsonObject.getString("info");
	    	            Toast.makeText(getActivity().getApplicationContext(), INFO,Toast.LENGTH_LONG).show();
	    	            }
    	            else
    	                result = "Did not work!";
    	            Log.d("++++++++++++++", result);
    	        } catch (Exception e) {
    	            Log.d("InputStream", e.getLocalizedMessage());
    	        }
        	}
        });
        return v;
    }

    public static FirstFragment newInstance(String text,String text2){

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("msg2", text2);
//        b.putString("msg3", text3);
//        b.putString("msg4", text4);
//        b.putString("msg5", text5);
//        b.putString("msg6", text6);
//        b.putString("msg7", text7);
        f.setArguments(b);

        return f;
    }
    
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        Log.e("src",src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        Log.e("Bitmap","returned");
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
	}
	private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {               
 
                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/"+getArguments().getString("msg2")+".jpg");
                    try 
                    {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(CompressFormat.JPEG, 75, ostream);
                        ostream.close();
                    } 
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }
 
                }
            }).start();
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }
 
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {
            }
        }
    };
}
