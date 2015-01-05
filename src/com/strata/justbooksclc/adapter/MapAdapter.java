package com.strata.justbooksclc.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
	public View getView(final int position, View view, ViewGroup parent) {
		
		LayoutInflater inflator = context.getLayoutInflater();
		View rowView = inflator.inflate(R.layout.map_list_item,null,false);
		TextView name = (TextView)rowView.findViewById(R.id.name);
		TextView address = (TextView)rowView.findViewById(R.id.address);
		TextView distance = (TextView)rowView.findViewById(R.id.distance);
		LinearLayout btn_call = (LinearLayout)rowView.findViewById(R.id.btn_call);
		address.setText(mapArry.get(position).getAddress());
		name.setText(mapArry.get(position).getName());
		distance.setText("distance : "+String.format( "%.2f",mapArry.get(position).getDistance())+" KM");
		
		btn_call.setOnClickListener(new Button.OnClickListener() {
        	@SuppressWarnings("deprecation")
			public void onClick(View v){
        		AlertDialog alert = new AlertDialog.Builder(context).create();
    	        alert.setTitle(mapArry.get(position).getName());
    	        alert.setMessage("Regular charges will apply");
    	        alert.setButton("Yes", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int which) {
    	        	   Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mapArry.get(position).getPhone()));
    				   call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				   context.startActivity(call);
    	           }
    	        });
    	        alert.setButton2("No",new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	            }
    	        });
    	        // Set the Icon for the Dialog
    	        alert.setIcon(R.drawable.gcm_icon);
    	        alert.show();
        	}
        });
		
		return rowView;
	}
}