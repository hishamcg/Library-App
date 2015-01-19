package com.strata.justbooksclc.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strata.justbooksclc.R;
import com.strata.justbooksclc.model.Review;

public class ReviewAdapter extends ArrayAdapter<Review> {
	private final Context context;
	private final ArrayList<Review> review_list;
	public ReviewAdapter(Context context, ArrayList<Review> review_list) {
		super(context, R.layout.review_list_item, review_list);
		this.context = context;
		this.review_list = review_list;
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Review review = review_list.get(position);
		View rowView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.review_list_item, parent,
				false);
		TextView Lheader  = (TextView) rowView.findViewById(R.id.id_publisher);
		TextView LContent  = (TextView) rowView.findViewById(R.id.id_content);

		Lheader.setText(review.getPublisher_name());
		LContent.setText(review.getReview_text());

		return rowView;
	}

}

