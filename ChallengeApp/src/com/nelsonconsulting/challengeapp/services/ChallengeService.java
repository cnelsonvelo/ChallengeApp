package com.nelsonconsulting.challengeapp.services;

import com.nelsonconsulting.challengeapp.data.ChallengeCollection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChallengeService extends Service {

	private ChallengeCollection challenges;
	
	@Override
	public void onCreate() {
		super.onCreate();
		challenges = new ChallengeCollection(getAssets());
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onStartCommand() {
		
	}
}
