package com.example.myfirstapp;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Book> {
	private final Activity context;
	private final Book[] bookArry;

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	public CustomAdapter(Activity context, Book[] bookArry) {
		super(context, R.layout.list_item, bookArry);
		this.context = context;
		this.bookArry = bookArry;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		/*
		 * LayoutInflater inflater = context.getLayoutInflater(); View rowView =
		 * inflater.inflate(R.layout.list_item, null, true);
		 */
		 LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    View rowView = inflater.inflate(R.layout.row_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView author = (TextView) rowView.findViewById(R.id.author);
		TextView category = (TextView) rowView.findViewById(R.id.category);
		TextView publisher = (TextView) rowView.findViewById(R.id.publisher);
		TextView price = (TextView) rowView.findViewById(R.id.price);
		TextView isbn = (TextView) rowView.findViewById(R.id.isbn);

		Book book = bookArry[position];
		new DownloadImageTask((ImageView) rowView.findViewById(R.id.image))
		 .execute(book.getImageUrl());
		title.setText(book.getTitle());
		author.setText(book.getAuthor());
		category.setText(book.getCategory());
		publisher.setText(book.getPublisher());
  		price.setText(book.getPrice());
  		isbn.setText(book.getIsbn());

		return rowView;
	}
}

/*
 * package learn2crack.customlistview; import android.app.Activity; import
 * android.view.LayoutInflater; import android.view.View; import
 * android.view.ViewGroup; import android.widget.ArrayAdapter; import
 * android.widget.ImageView; import android.widget.TextView;
 */