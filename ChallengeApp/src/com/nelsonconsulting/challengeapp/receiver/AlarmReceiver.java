package com.nelsonconsulting.challengeapp.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nelsonconsulting.challengeapp.Constants;
import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.data.ChallengeCollection;
import com.nelsonconsulting.challengeapp.preferences.TimePreference;
import com.nelsonconsulting.challengeapp.services.ChallengeService;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String INTENT = "com.nelsonconsulting.intent.AlarmWakeup";
	
	private SharedPreferences preferences;
	private ChallengeCollection challenges;
	private PendingIntent pi;
	
	private static AlarmReceiver instance;
	private static final int WAKEUP_INTERVAL = 10000;
	
	public static AlarmReceiver getInstance() {		
		return instance;
	}
	
	public static void initialize(Context context) {
	
		if (instance == null) {
			instance = new AlarmReceiver(context);
		}
	}
	
	private AlarmReceiver(Context context) {
		super();
		Log.i(Constants.APP_LOG_TAG, "Creating Alarm Receiver");
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		// load the challenges and determine the correct wake-up interval
		challenges = ChallengeCollection.getInstance(context.getAssets());
		
		// setup the alarm
		context.registerReceiver(this, new IntentFilter(INTENT));
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		pi = PendingIntent.getBroadcast(context, 0, new Intent(INTENT), 0);
		am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + WAKEUP_INTERVAL, WAKEUP_INTERVAL, pi);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {	
	
		// check if this is during the day, and if so, send a message to challenge service
		Log.i(Constants.APP_LOG_TAG, "Receiving Alarm Wakeup...");
		if (isDuringDay()) {
			Intent svcIntent = new Intent(context, ChallengeService.class);
			context.startService(svcIntent);
		}
	}
	
	private boolean isDuringDay() {
		String startTime = preferences.getString(Constants.PREF_START_TIME, "08:00");
		int hours = preferences.getInt(Constants.PREF_HOURS, 9);
		
		// the current time
		Calendar current = Calendar.getInstance();
		
		// the start time
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(startTime));
		start.set(Calendar.MINUTE, TimePreference.getMinute(startTime));
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		
		// the end time
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(startTime));
		end.set(Calendar.MINUTE, TimePreference.getMinute(startTime));
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);
		end.add(Calendar.HOUR, hours);
		
		SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.US);
		Log.v(Constants.APP_LOG_TAG, String.format("Checking day arguments: %s-%s-%s", format.format(start.getTime()), 
				format.format(current.getTime()), 
				format.format(end.getTime())));
		if (current.before(start) || current.after(end))
			return false;
		return true;
	}
}
