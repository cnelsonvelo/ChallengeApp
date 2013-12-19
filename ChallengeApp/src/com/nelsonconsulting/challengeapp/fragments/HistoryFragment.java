package com.nelsonconsulting.challengeapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.adapters.HistoryListAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

public class HistoryFragment extends Fragment {
	public static int PAGE_TITLE_ID = R.string.page_title_history_fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, container, false);
		
		// TODO: remove the junk data
		List<String> headers = new ArrayList<String>();
		headers.add("Day 1");
		headers.add("Day 2");
		headers.add("Day 3");
		Map<String,  List<String>> data = new HashMap<String, List<String>>();
		List<String> day = new ArrayList<String>();
		day.add("Did 44 push-ups");
		day.add("Did 45 push-ups");
		day.add("Did 46 push-ups");
		data.put(headers.get(0), day);
		data.put(headers.get(1), day);
		data.put(headers.get(2), day);
		List<String> intervals = new ArrayList<String>();
		intervals.add("Week 1 (January 1 - 7, 2013");
		intervals.add("Week 2 (January 8 - 14, 2013");
		intervals.add("Week 3 (January 15 - 21, 2013");
		intervals.add("Week 4 (January 22 - 28, 2013");
		
		// get the spinner and set the adapter
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(view.getContext(), 
				android.R.layout.simple_spinner_item);
		spinAdapter.addAll(intervals);
		spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner)view.findViewById(R.id.spinner_history);
		spinner.setAdapter(spinAdapter);
		
		// get the expandable list and set the adapter
		HistoryListAdapter listAdapter = new HistoryListAdapter(view.getContext(), headers, data);
		ExpandableListView lv = (ExpandableListView)view.findViewById(R.id.list_history);
		lv.setAdapter(listAdapter);
		
		return view;
	}
}
