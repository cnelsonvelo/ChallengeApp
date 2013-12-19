package com.nelsonconsulting.challengeapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nelsonconsulting.challengeapp.fragments.SelectDateFragment;
import com.nelsonconsulting.challengeapp.fragments.SetFragment;
import com.nelsonconsulting.challengeapp.fragments.TestFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
	
	private static int PAGE_COUNT = 3;
	
	private Context context;
	private SelectDateFragment selectDateFragment;
	private SetFragment setFragment;
	private TestFragment testFragment;

	public MainActivityPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		selectDateFragment = new SelectDateFragment();
		setFragment = new SetFragment(); 
		testFragment = new TestFragment();
		this.context = context;
	}

	@Override
	public Fragment getItem(int page) {

		switch(page) {
		case 0:
			return selectDateFragment;
		case 1:
			return testFragment;
		case 2:
			return setFragment;
		}
		
		return null;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch(position) {
		case 0:
			return context.getString(SelectDateFragment.PAGE_TITLE_ID);
		case 1:
			return context.getString(TestFragment.PAGE_TITLE_ID);
		case 2:
			return context.getString(SetFragment.PAGE_TITLE_ID);
		}
		
		return "";
	}
}
