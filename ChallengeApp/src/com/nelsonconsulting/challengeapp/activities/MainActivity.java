package com.nelsonconsulting.challengeapp.activities;

import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.R.layout;
import com.nelsonconsulting.challengeapp.R.menu;
import com.nelsonconsulting.challengeapp.adapters.MainActivityPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// get the view pager
		ViewPager pager = (ViewPager)findViewById(R.id.pager_main_activity);
		
		// get the fragment manager
		FragmentManager fm = getSupportFragmentManager();
		
		// setup the pager adapter
		MainActivityPagerAdapter adapter = new MainActivityPagerAdapter(fm, this);
		pager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
