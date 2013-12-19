package com.nelsonconsulting.challengeapp.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nelsonconsulting.challengeapp.R;

public class HistoryListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> headers;
	private Map<String, List<String>> data;
	
	public HistoryListAdapter(Context context, List<String> headers, Map<String, List<String>> data) {
		this.context = context;
		this.headers = headers;
		this.data = data;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(headers.get(groupPosition)).get(childPosition);
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
			convertView = inflater.inflate(R.layout.list_history_item, null);
		}
		
		// set the item text
		TextView tv = (TextView)convertView.findViewById(R.id.text_history_item);
		tv.setText((String)getChild(groupPosition, childPosition));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(headers.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return headers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return headers.size();
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
			convertView = inflater.inflate(R.layout.list_history_header, null);
		}
		
		// set the item text
		TextView tv = (TextView)convertView.findViewById(R.id.text_history_header);
		tv.setText((String)getGroup(groupPosition));
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
