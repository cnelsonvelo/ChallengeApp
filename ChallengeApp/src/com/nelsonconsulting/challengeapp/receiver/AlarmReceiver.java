package com.nelsonconsulting.challengeapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String INTENT = "com.nelsonconsulting.intent.AlarmWakeup";
	
	public AlarmReceiver(Context context) {
		context.registerReceiver(this, new IntentFilter(INTENT));
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {

	}
}
