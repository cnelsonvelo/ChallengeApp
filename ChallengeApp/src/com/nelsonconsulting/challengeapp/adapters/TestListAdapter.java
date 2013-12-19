package com.nelsonconsulting.challengeapp.adapters;

import java.util.List;
import java.util.Map;

import com.nelsonconsulting.challengeapp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class TestListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> header;
	private Map<String, List<String>> data;
	
	public TestListAdapter(Context context, List<String> header, Map<String, List<String>> data) {
		this.context = context;
		this.header = header;
		this.data = data;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(header.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		
		// create the child view if it is not already
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_testinfo, null);
		}
		
		// set the item text
		TextView tv = (TextView)convertView.findViewById(R.id.text_testinfo_item);
		tv.setText((String)getChild(groupPosition, childPosition));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(header.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return header.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return header.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		// create the child view if it is not already
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_header_textinfo, null);
		}
		
		// set the item text
		TextView tv = (TextView)convertView.findViewById(R.id.text_testinfo_header);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setText((String)getGroup(groupPosition));
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
