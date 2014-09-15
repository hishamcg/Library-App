package com.strata.justbooksclc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
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
import android.widget.TextView;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class FirstFragment extends Fragment {
	private static final String TAG_AUTHOR = "author";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PAGE = "no_of_pages";
	private static final String TAG_IMAGE_URL = "image_url";
	private static final String TAG_LANGUAGE = "language";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ID = "title_id";
	private static final String SUMMARY = "summary";
	private static final String TIMES_RENTED = "no_of_times_rented";
	private static final String AVG_READING = "avg_reading_times";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);

        TextView tv = (TextView) v.findViewById(R.id.times_rented);
        tv.setText(getArguments().getString("times_rented"));

        TextView tv2 = (TextView) v.findViewById(R.id.avg_reading);
        tv2.setText(getArguments().getString("avg_reading"));

        TextView tv4 = (TextView) v.findViewById(R.id.author_label);
        tv4.setText(getArguments().getString("author"));

        TextView tv5 = (TextView) v.findViewById(R.id.title_label);
        tv5.setText(getArguments().getString("title"));
        
        TextView tv7 = (TextView) v.findViewById(R.id.category_label);
        tv7.setText(getArguments().getString("category"));

        ImageView tv6 = (ImageView) v.findViewById(R.id.image_label);
        final ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.loading);

        Picasso.with(this.getActivity())
        .load(getArguments().getString("image_url"))
        .error(R.drawable.book)
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
        .load(getArguments().getString("image_url"))
        .into(target);

        final LinearLayout click_image =(LinearLayout) v.findViewById(R.id.click_image);

        click_image.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View view){
    			Intent in = new Intent(getActivity().getApplicationContext(),SingleMenuItemActivity.class);
    			in.putExtra(TAG_AUTHOR, getArguments().getString("author"));
    			in.putExtra(TAG_CATEGORY, getArguments().getString("category"));
    			in.putExtra(TAG_TITLE, getArguments().getString("title"));
    			in.putExtra(TAG_LANGUAGE, getArguments().getString("language"));
    			in.putExtra(TAG_PAGE, getArguments().getString("page"));
    			in.putExtra(TAG_IMAGE_URL, getArguments().getString("image_url"));
    			in.putExtra(TAG_ID, getArguments().getString("tag_id"));
    			in.putExtra(TIMES_RENTED, getArguments().getString("times_rented"));
    			in.putExtra(AVG_READING, getArguments().getString("avg_reading"));
    			in.putExtra(SUMMARY, getArguments().getString("summary"));
    			in.putExtra("message", "create");
    			in.putExtra("check","logged_in");
    			startActivity(in);
        	}
        });
        
        return v;
   }

    public static FirstFragment newInstance(String text,String text2,String text3,String text4,String text5,String text6,String text7,String text8,String text9,String text10){

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("image_url", text);
        b.putString("tag_id", text2);
        b.putString("times_rented", text3);
        b.putString("avg_reading", text4);
        b.putString("author", text5);
        b.putString("title", text6);
        b.putString("category", text7);
        b.putString("page", text8);
        b.putString("language", text9);
        b.putString("summary", text10);



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
