package com.nelsonconsulting.challengeapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.adapters.TestListAdapter;

public class TestFragment extends Fragment {
	
	public static int PAGE_TITLE_ID = R.string.page_title_test;
	
	private TestListAdapter adapter;
	private List<String> listHeaders;
	private Map<String, List<String>> listData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_test_info, container, false);
		
		// TODO: adding a fake description to the view
		TextView tv = (TextView)view.findViewById(R.id.text_testinfo_description);
		tv.setText("Fake description for testing purposes");
		
		// TODO: adding a set of fake tests to the view
		listHeaders = new ArrayList<String>();
		listHeaders.add("8:00AM");
		listHeaders.add("12:00PM");
		listHeaders.add("4:00PM");
		listData = new HashMap<String, List<String>>();
		List<String> times1 = new ArrayList<String>();
		times1.add("Did 45 pushups in 60 seconds.");
		List<String> times2 = new ArrayList<String>();
		times2.add("Did 40 pushups in 60 seconds.");
		List<String> times3 = new ArrayList<String>();
		times3.add("Did 43 pushups in 60 seconds.");
		listData.put("8:00AM", times1);
		listData.put("12:00PM", times2);
		listData.put("4:00PM", times3);
		
		ExpandableListView lv = (ExpandableListView)view.findViewById(R.id.listview_testinfo);
		adapter = new TestListAdapter(container.getContext(), listHeaders, listData);
		lv.setAdapter(adapter);
		
		//lv.setAdapter(new ArrayAdapter<String>(this.getActivity(), R.id.listview_testinfo, new String[] { "Test1", "Test2", "Test3" }));
		
		return view;
	}
}
