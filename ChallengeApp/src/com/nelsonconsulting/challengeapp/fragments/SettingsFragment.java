package com.nelsonconsulting.challengeapp.fragments;

import com.nelsonconsulting.challengeapp.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// load the preferences from the resource file
		addPreferencesFromResource(R.xml.preferences);
	}
}
