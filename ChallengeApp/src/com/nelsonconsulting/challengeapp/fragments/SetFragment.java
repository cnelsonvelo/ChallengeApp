package com.nelsonconsulting.challengeapp.fragments;

import java.util.ArrayList;
import java.util.List;

import com.nelsonconsulting.challengeapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetFragment extends Fragment {

	public static int PAGE_TITLE_ID = R.string.page_title_set_fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set_activity, container, false);
		
		// TODO: remove junk data
		List<String> intervals = new ArrayList<String>();
		intervals.add("Test - 8am");
		intervals.add("Set - 9am");
		intervals.add("Set - 10am");
		
		// get the spinner and set the adapter
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(view.getContext(), 
				android.R.layout.simple_spinner_item);
		spinAdapter.addAll(intervals);
		spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner)view.findViewById(R.id.spinner_set);
		spinner.setAdapter(spinAdapter);
		
		return view;
	}
}
