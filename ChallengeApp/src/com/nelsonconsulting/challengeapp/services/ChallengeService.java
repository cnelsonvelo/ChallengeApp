package com.nelsonconsulting.challengeapp.services;

import java.util.Calendar;
import java.util.List;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.nelsonconsulting.challengeapp.Constants;
import com.nelsonconsulting.challengeapp.Constants.ChallengeStatus;
import com.nelsonconsulting.challengeapp.Constants.SetStatus;
import com.nelsonconsulting.challengeapp.Constants.Type;
import com.nelsonconsulting.challengeapp.R;
import com.nelsonconsulting.challengeapp.data.Challenge;
import com.nelsonconsulting.challengeapp.data.ChallengeCollection;
import com.nelsonconsulting.challengeapp.data.ChallengeDB;
import com.nelsonconsulting.challengeapp.data.ChallengeDB.SetInfo;
import com.nelsonconsulting.challengeapp.data.DataHelper;

public class ChallengeService extends Service {

	private Looper looper;
	private ServiceHandler handler;
	private SharedPreferences preferences;
	
	private final class ServiceHandler extends Handler {
		
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			ChallengeDB db = ChallengeDB.getInstance(getApplicationContext());
			List<ChallengeDB.ChallengeInfo> active = db.getChallenges(ChallengeStatus.Running);
			
			synchronized(this) {
				// if no challenges are active, then return without doing anything
				if (active.size() == 0)
					return;
			
				Calendar current = Calendar.getInstance();
				
				// loop through the active challenges and see if any notifications are necessary
				for (ChallengeDB.ChallengeInfo cInfo : active) {
					
					// get the challenge for this entry
					Challenge challenge = ChallengeCollection.getInstance(getAssets()).getChallenge(cInfo.getName());
					
					// determine which day we are currently on
					int currentChallengeDay = current.get(Calendar.DAY_OF_YEAR) - cInfo.getStartDate().get(Calendar.DAY_OF_YEAR) + 1;			
					
					handleTests(cInfo.getId(), challenge, currentChallengeDay);
					handleSets(cInfo.getId(), challenge, currentChallengeDay);
					
					// show the notifications if necessary
					createNotification(cInfo.getId(), currentChallengeDay);
				}
			}
			
			stopSelf(msg.arg1);
		}
		
		private void createNotification(int challengeId, int currentDay) {
			
			// get the sets waiting notifications
			List<SetInfo> waitTests = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentDay, Type.Test, SetStatus.Notified);
			List<SetInfo> newTests = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentDay, Type.Test, SetStatus.New);
			for (SetInfo s : newTests) {
				s.setStatus(SetStatus.Notified);
				ChallengeDB.getInstance(getApplicationContext()).updateSet(s);
			}
			
			List<SetInfo> waitSets = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentDay, Type.Normal, SetStatus.Notified);
			List<SetInfo> newSets = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentDay, Type.Normal, SetStatus.New);
			for (SetInfo s : newSets) {
				s.setStatus(SetStatus.Notified);
				ChallengeDB.getInstance(getApplicationContext()).updateSet(s);
			}
			
			// get the content string
			String content = "";
			if (newTests.size() == 0 && newSets.size() == 0) {
				return;
			}
			else if ((waitTests.size() != 0 || newTests.size() != 0) && (waitSets.size() == 0 && newSets.size() == 0)) {
				content = getString(R.string.notification_test_content);
			}
			else if ((waitTests.size() == 0 && newTests.size() == 0) && (waitSets.size() != 0 || newSets.size() != 0)) {
				content = getString(R.string.notification_single_content);
			}
			else {
				content = getString(R.string.notification_multiple_content);
			}
			
			// build the inbox style content
			//NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();
			//inbox.setBigContentTitle(getString(R.string.notification_title));
			//for (String s : sets) {
				//inbox.addLine(s);
			//}
			
			// build the notification
			NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.notification_title))
				//.setStyle(inbox)
				.setContentText(content);
			
			// issue the notification
			NotificationManager mgr = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
			mgr.notify(0, builder.build());
		}
		
		private boolean handleSets(int challengeId, Challenge challenge, int currentChallengeDay) {
			
			boolean setCreated = false;
			
			// figure out which interval is currently active
			double rem = Math.IEEEremainder(currentChallengeDay, challenge.getSetInfo().size());
			int currentInterval = (int) rem;
			if (rem == 0) 
				currentInterval = challenge.getSetInfo().size();
			
			// get the current set we should be on for the interval
			String dayStartTime = preferences.getString(Constants.PREF_START_TIME, "08:00");
			int set = DataHelper.getCurrentIntervalSet(dayStartTime, challenge, currentInterval);
			
			// adjust the number of sets by the number of tests
			List<SetInfo> testSets = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentChallengeDay, Type.Test);
			set -= testSets.size();	
			
			// get the sets (both test and normal) currently in the db
			List<SetInfo> sets = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentChallengeDay, Type.Normal);
			for (int i = sets.size(); i <= set; i++) {
				int value = DataHelper.getSetValue(challengeId, challenge.getSetInfo().get(currentInterval));
				ChallengeDB.getInstance(getApplicationContext()).addSetEntry(challengeId, currentChallengeDay, Type.Normal, value);
				setCreated = true;
			}
			
			return setCreated;
		}
	
		private boolean handleTests(int challengeId, Challenge challenge, int currentChallengeDay) {
		
			// determine the interval
			int interval = -1;
			switch (challenge.getTestInfo().getIntervalType()) {
			case Weekly:
				interval = 7;
				break;
			case Daily:
				interval = 1;
				break;
			default:
				Log.e(Constants.APP_LOG_TAG, "Only weekly and hourly are supported test intervals.");
				return false;
			}
			interval = 1;
			
			// determine how many tests should be in the database at this point
			double rem = Math.IEEEremainder(currentChallengeDay, interval);
			if (rem == 0) {
				List<SetInfo> tests = ChallengeDB.getInstance(getApplicationContext()).getSets(challengeId, currentChallengeDay, Type.Test);
				if (tests.size() == 0)
				{
					ChallengeDB.getInstance(getApplicationContext()).addSetEntry(challengeId, currentChallengeDay, Type.Test, -1);
					return true;
				}
			}

			return false;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
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
