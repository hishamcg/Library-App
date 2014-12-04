package com.strata.justbooksclc.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback.EmptyCallback;
import com.squareup.picasso.Picasso;
import com.strata.justbooksclc.R;
import com.strata.justbooksclc.model.Book;

public class WishlistAdapter extends ArrayAdapter<Book> {
	private final Activity context;
	ArrayList<Book> bookArry;
	//= new ArrayList<Book>();
	private final int numberOfRows;
	private final int numberOfColumns;
	private final int heightInPixels;
	int numberOfBooks;
	String my_theme;

	public WishlistAdapter(Activity context, ArrayList<Book> bookArry,int numberOfRows,int numberOfColumns,int heightInPixels) {
		super(context, R.layout.grid_item, bookArry);
		this.context = context;
		this.bookArry = bookArry;
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.heightInPixels = heightInPixels;
		
		SharedPreferences value = context.getSharedPreferences("PREF", Context.MODE_PRIVATE);
		my_theme = value.getString("MY_THEME", "");
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater inflator = context.getLayoutInflater();
		View rowView = inflator.inflate(R.layout.grid_item,null,false);
		rowView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, heightInPixels));
		final ProgressBar progressBar= (ProgressBar) rowView.findViewById(R.id.loading);
		ImageView imageView = (ImageView)rowView.findViewById(R.id.image_grid);
		LinearLayout shelf_image = (LinearLayout)rowView.findViewById(R.id.shelf_image);
		
        if (my_theme.equals("green"))
        	shelf_image.setBackgroundResource( R.drawable.book_shelf2 );
		else if (my_theme.equals("brown"))
			shelf_image.setBackgroundResource( R.drawable.book_shelf5 );
		else if (my_theme.equals("violet"))
			shelf_image.setBackgroundResource( R.drawable.book_shelf4 );
		else if (my_theme.equals("blue"))
			shelf_image.setBackgroundResource( R.drawable.book_shelf3 );
		else
			shelf_image.setBackgroundResource( R.drawable.book_shelf1 );
		
		if(position < numberOfBooks){
			Book book = bookArry.get(position);
			/*if (view == null) {
			      imageView = new ImageView(context);
			      imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			      imageView.setPadding(8, 8, 8, 8);
			      } else {
			      imageView = (ImageView) view;
			      }*/
			//--------------------------
			Picasso.with(context)
			.load(book.getImage_url())
			.error(R.drawable.ic_launcher)
			.placeholder(R.drawable.ic_launcher)
			.into(imageView, new EmptyCallback() {
	            @Override public void onSuccess() {
	                progressBar.setVisibility(View.GONE);
	            }
	            @Override
	            public void onError() {
	                progressBar.setVisibility(View.GONE);
	            }
	        });
		}else if(position == numberOfBooks){
			ImageView imageViewCover = (ImageView)rowView.findViewById(R.id.image_cover);
			imageView.setBackgroundResource(R.drawable.book_cover);
			progressBar.setVisibility(View.GONE);
			imageViewCover.setVisibility(View.GONE);
		}else{
			ImageView imageViewCover = (ImageView)rowView.findViewById(R.id.image_cover);
			progressBar.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
			imageViewCover.setVisibility(View.GONE);
			
			//make it non clickable

			rowView.setEnabled(false);
			rowView.setOnClickListener(null);
		}
		return rowView;
	}
	
	@Override
    public int getCount() {
		int numberOfCells = numberOfRows*numberOfColumns;
		numberOfBooks = bookArry.size();
		if (numberOfRows*numberOfColumns < numberOfBooks){
			numberOfCells = numberOfBooks + (3-numberOfBooks%numberOfRows);
		}
        return numberOfCells;
    }
}