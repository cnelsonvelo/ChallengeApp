package com.nelsonconsulting.challengeapp.fragments;

import com.nelsonconsulting.challengeapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SetFragment extends Fragment {

	public static int PAGE_TITLE_ID = R.string.page_title_set_fragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set_activity, container, false);
		
		return view;
	}
}
