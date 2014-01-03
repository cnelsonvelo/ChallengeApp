package com.nelsonconsulting.challengeapp.services;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.nelsonconsulting.challengeapp.Constants;
import com.nelsonconsulting.challengeapp.Constants.ChallengeStatus;
import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.data.Challenge;
import com.nelsonconsulting.challengeapp.data.ChallengeCollection;
import com.nelsonconsulting.challengeapp.data.ChallengeDB;
import com.nelsonconsulting.challengeapp.data.ChallengeDB.ChallengeInfo;
import com.nelsonconsulting.challengeapp.data.ChallengeDB.SetInfo;
import com.nelsonconsulting.challengeapp.preferences.TimePreference;

public class ChallengeService extends Service {

	private ChallengeCollection challenges;
	private ChallengeDB db;
	private Looper looper;
	private ServiceHandler handler;
	private SharedPreferences preferences;
	
	private final class ServiceHandler extends Handler {
		
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			List<ChallengeDB.ChallengeInfo> active = db.getChallenges(ChallengeStatus.Running);
			
			synchronized(this) {
				// if no challenges are active, then return without doing anything
				if (active.size() == 0)
					return;
			
				Calendar current = Calendar.getInstance();
				
				// loop through the active challenges and see if any notifications are necessary
				for (ChallengeDB.ChallengeInfo cInfo : active) {
					
					// determine which day we are currently on
					int day = current.get(Calendar.DAY_OF_YEAR) - cInfo.getStartDate().get(Calendar.DAY_OF_YEAR) + 1;
					
					// get the current set for the day
					int set = getIntervalSet(cInfo, current, day);
					
					// get the sets currently in the db
					List<SetInfo> sets = db.getSets(cInfo.getName(), day);
					
					// if the number of sets recorded in the db is less than the current, then add a new set to the db, and 
					// notify the user
					if (sets.size() < set) {
						NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
							.setSmallIcon(R.drawable.ic_launcher)
							.setContentTitle("Test Notification")
							.setContentText("You have a set to do!");
						NotificationManager mgr = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
						mgr.notify(0, builder.build());
					}
				}
			}
			
			stopSelf(msg.arg1);
		}
		
		private int getIntervalSet(ChallengeInfo challenge, Calendar current, int day) {
			
			// determine how many minutes into the day we are
			String startString = preferences.getString(Constants.PREF_START_TIME, "08:00");
			Calendar startTime = Calendar.getInstance();
			startTime.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(startString));
			startTime.set(Calendar.MINUTE, TimePreference.getMinute(startString));
			startTime.set(Calendar.SECOND, 0);
			startTime.set(Calendar.MILLISECOND, 0);
			int minutes = (int)(current.getTimeInMillis() - startTime.getTimeInMillis()) / (1000 * 60);
			
			// determine how may sets have passed for the day
			int set = (int)(minutes / getSetTimeInMinutes(challenge, day)) + 1;
		
			return set;
		}
		
		private int getSetTimeInMinutes(ChallengeInfo challenge, int day) {
			Challenge c = challenges.getChallenge(challenge.getName());
			switch (c.getSetInfo().get(day).getIntervalType()) {
			case Hourly:
				return 60;
			case Daily:
				return 1440;
			case Weekly:
				return 10080;
			case Specific:
				return c.getSetInfo().get(day).getIntervalValue();
			}
			
			return -1;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		challenges = ChallengeCollection.getInstance(getAssets());
		db = ChallengeDB.getInstance(getApplicationContext());
		
		// create the new thread
		HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		
		// create the handler
		looper = thread.getLooper();
		handler = new ServiceHandler(looper);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// create a new message for the handler
		Message msg = handler.obtainMessage();
		msg.arg1 = startId;
		handler.sendMessage(msg);
		
		return START_STICKY;
	}
}
