package com.strata.justbooksclc.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.model.Book;

public class CustomAdapter extends ArrayAdapter<Book> {
	private final Context context;
	ArrayList<Book> bookArry;

	public CustomAdapter(Context context, ArrayList<Book> bookArry) {
		super(context, R.layout.book_list_single_item, bookArry);
		this.context = context;
		this.bookArry = bookArry;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		 LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.book_list_single_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView author = (TextView) rowView.findViewById(R.id.author);
		TextView times_rented = (TextView) rowView.findViewById(R.id.times_rented);
		TextView avg_reading = (TextView) rowView.findViewById(R.id.avg_reading);
		TextView pickup_order = (TextView) rowView.findViewById(R.id.pickup_order);
		LinearLayout detail_mini = (LinearLayout) rowView.findViewById(R.id.detail_mini);

		Book book = bookArry.get(position);
		ImageView image = (ImageView) rowView.findViewById(R.id.image);
        final ProgressBar progressBar= (ProgressBar) rowView.findViewById(R.id.progressBar1);
		//--------------------------
		Picasso.with(context)
		.load(book.getImage_url())
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
  		times_rented.setText(book.getTimes_rented());
  		avg_reading.setText(book.getAvg_reading());
  		if (book.getPickup_order() != null && !book.getPickup_order().equals("null")){
  			pickup_order.setText("pickup in process...");
  			pickup_order.setVisibility(View.VISIBLE);
  			detail_mini.setVisibility(View.GONE);
  		}

		return rowView;
	}
}