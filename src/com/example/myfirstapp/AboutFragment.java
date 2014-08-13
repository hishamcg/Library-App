package com.example.myfirstapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AboutFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.help_activity, container, false);
         
        return rootView;
    }
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		final Button btn_care = (Button) view.findViewById(R.id.btn_care);
		btn_care.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				 String phoneNumber = "08030752626";
			     Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
			     call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			     startActivity(call);
			}
		});
	}
}

