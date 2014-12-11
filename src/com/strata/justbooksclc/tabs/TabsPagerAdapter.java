package com.strata.justbooksclc.tabs;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.strata.justbooksclc.Config;
import com.strata.justbooksclc.fragment.BooksOurPickFragment;


public class TabsPagerAdapter extends FragmentPagerAdapter {
//	public TabsPagerAdapter(android.support.v4.app.Fragment fragment){
//	    super(fragment.getChildFragmentManager());
//	}
	Context mcontext;
	public TabsPagerAdapter(FragmentManager fm,Context context) {
		super(fm);
		this.mcontext = context;
	}

	@Override
	public Fragment getItem(int index) {
		SharedPreferences value = mcontext.getSharedPreferences("PREF", Context.MODE_PRIVATE);
		String numb = value.getString("NUMBER", "");
		String memb = value.getString("MEMBERSHIP_NO", "");
		String auth_token = value.getString("AUTH_TOKEN", "");
		//int last_data_fetch = value.getInt("DATA_FETCH_DATE", 0);
		Calendar c = Calendar.getInstance();
		int current_date = c.get(Calendar.WEEK_OF_YEAR)*7+c.get(Calendar.DATE);

		String url = "";
		//if (last_data_fetch != current_date){
			
			if(index == 0){
				if (numb != null && numb != ""){
					url = "http://"+Config.SERVER_BASE_URL+"/api/v1/your_next_read.json?api_key="+auth_token+"&phone="+numb+"&membership_no="+memb;
				}else{
					url= "http://"+Config.SERVER_BASE_URL+"/api/v1/your_next_read.json";
				}
			}else if(index == 1){
				url = "http://"+Config.SERVER_BASE_URL+"/api/v1/top_rentals.json";
			}else if(index == 2){
				url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json";
				SharedPreferences.Editor   editor = value.edit();
				editor.putInt("DATA_FETCH_DATE", current_date);
				editor.commit();
			}else{
				//default load new arrivals
				url = "http://"+Config.SERVER_BASE_URL+"/api/v1/new_arrivals.json";
			}
		//}
		Bundle bundle = new Bundle();
		if(index >=0 && index <3){
			BooksOurPickFragment frag_next = new BooksOurPickFragment();
			bundle.putString("url", url);
			bundle.putInt("db_table_key", index);
			frag_next.setArguments(bundle);
			return frag_next;
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	

}
