package com.strata.justbooksclc.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.strata.justbooksclc.R;
import com.strata.justbooksclc.model.MapArray;

public class MapAdapter extends ArrayAdapter<MapArray> {
	private final Activity context;
	ArrayList<MapArray> mapArry;

	public MapAdapter(Activity context, ArrayList<MapArray> mapArry) {
		super(context, R.layout.map_list_item, mapArry);
		this.context = context;
		this.mapArry = mapArry;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater inflator = context.getLayoutInflater();
		View rowView = inflator.inflate(R.layout.map_list_item,null,false);
		TextView text1 = (TextView)rowView.findViewById(android.R.id.text1);
		TextView text2 = (TextView)rowView.findViewById(android.R.id.text2);
		text1.setText(mapArry.get(position).getName());
		text2.setText("distance : "+String.format( "%.2f",mapArry.get(position).getDistance())+" KM");

		return rowView;
	}
}