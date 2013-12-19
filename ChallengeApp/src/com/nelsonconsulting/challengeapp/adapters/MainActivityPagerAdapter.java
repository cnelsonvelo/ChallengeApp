package com.nelsonconsulting.challengeapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nelsonconsulting.challengeapp.fragments.HistoryFragment;
import com.nelsonconsulting.challengeapp.fragments.SetFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
	
	private static int PAGE_COUNT = 2;
	
	private Context context;
	private HistoryFragment historyFragment;
	private SetFragment setFragment;

	public MainActivityPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		setFragment = new SetFragment(); 
		historyFragment = new HistoryFragment();
		this.context = context;
	}

	@Override
	public Fragment getItem(int page) {

		switch(page) {
		case 0:
			return setFragment;
		case 1:
			return historyFragment;
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
			return context.getString(SetFragment.PAGE_TITLE_ID);
		case 1:
			return context.getString(HistoryFragment.PAGE_TITLE_ID);
		}
		
		return "";
	}
}
