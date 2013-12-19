package com.nelsonconsulting.challengeapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nelsonconsulting.challengeapp.R;

public class SelectDateFragment extends Fragment {
	
	public static int PAGE_TITLE_ID = R.string.page_title_select_date_fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_select_date, container, false);
		
		// set the info text
		TextView tv = (TextView)view.findViewById(R.id.text_info);
		tv.setText("Select the date you would like to see the challenge information for:");
		
		return view;
	}
}
