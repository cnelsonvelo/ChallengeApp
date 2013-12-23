package com.nelsonconsulting.challengeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.adapters.MainActivityPagerAdapter;
import com.nelsonconsulting.challengeapp.data.ChallengeCollection;

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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			changeSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void changeSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
}
