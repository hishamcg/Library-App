package com.example.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;

public class CustomAdapter extends ArrayAdapter<Book> {
	private final Activity context;
	private final Book[] bookArry;

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
		TextView title_id = (TextView) rowView.findViewById(R.id.title_id);
		TextView rental_id = (TextView) rowView.findViewById(R.id.rental_id);
		TextView times_rented = (TextView) rowView.findViewById(R.id.times_rented);
		TextView avg_reading = (TextView) rowView.findViewById(R.id.avg_reading);

		Book book = bookArry[position];
		ImageView image = (ImageView) rowView.findViewById(R.id.image);
        final ProgressBar progressBar= (ProgressBar) rowView.findViewById(R.id.progressBar1);
		//--------------------------
		Picasso.with(context)
		.load(book.getImageUrl())
		.error(R.drawable.ic_launcher)
		.into(image, new EmptyCallback() {
            @Override public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }            
            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }            
        });
		//--------------------------
		title.setText(book.getTitle());
		author.setText(book.getAuthor());
		category.setText(book.getCategory());
		publisher.setText(book.getPublisher());
  		price.setText(book.getPrice());
  		isbn.setText(book.getIsbn());
  		title_id.setText(book.getId());
  		rental_id.setText(book.getRental_id());
  		times_rented.setText(book.getTimes_rented());
  		avg_reading.setText(book.getAvg_reading());

		return rowView;
	}
}