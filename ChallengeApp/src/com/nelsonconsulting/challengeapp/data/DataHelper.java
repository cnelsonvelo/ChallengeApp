package com.nelsonconsulting.challengeapp.data;

import java.util.Calendar;
import java.util.List;

import com.nelsonconsulting.challengeapp.Constants.IntervalType;
import com.nelsonconsulting.challengeapp.Constants.Type;
import com.nelsonconsulting.challengeapp.data.ChallengeDB.SetInfo;
import com.nelsonconsulting.challengeapp.preferences.TimePreference;

public class DataHelper {
	
	public static int getCurrentInterval(Challenge challenge, int challengeDay) {			
		// right now we only support sets the operate on a daily schedule
		double rem = Math
				.IEEEremainder(challengeDay, challenge.getSetInfo().size());
		if (rem <= 0) 
			return challengeDay;
		else
			return (int)rem;
	}
	
	public static int getCurrentIntervalSet(String dayStartTime, Challenge challenge, int interval) {
		
		int set = 0;
		Calendar current = Calendar.getInstance();
		
		// get the correct set information
		Challenge.SetInfo sInfo = challenge.getSetInfo().get(interval);
		
		// if the interval is less than a day, then we worry about the day length, etc... otherwise we just worry about total days
		if (sInfo.getIntervalType() == IntervalType.Hourly || sInfo.getIntervalType() == IntervalType.Specific) { 
		
			// determine how many minutes into the day we are
			Calendar startTime = Calendar.getInstance();
			startTime.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(dayStartTime));
			startTime.set(Calendar.MINUTE, TimePreference.getMinute(dayStartTime));
			startTime.set(Calendar.SECOND, 0);
			startTime.set(Calendar.MILLISECOND, 0);
			int minutes = (int)(current.getTimeInMillis() - startTime.getTimeInMillis()) / (1000 * 60);
		
			// determine how may sets have passed for the day
			set = (int)(minutes / getSetTimeInMinutes(sInfo)) + 1;
		}
		else if (sInfo.getIntervalType() == IntervalType.Daily) {
			set = 1;
		}
	
		return set;
	}
	
	public static int getCurrentTest(Challenge challenge, int challengeDay) {
		int interval = 7;
		switch(challenge.getTestInfo().getIntervalType()) {
		case Hourly:
			interval = 1;
			break;
		default:
			interval = 7;
			break;
		}
		
		double rem = Math.IEEEremainder(challengeDay, interval);
		
		if (rem <= 0)
			return 1;
		else
			return (int)rem;
	}
	
	public static int getSetValue(int challengeId, Challenge.SetInfo setInfo) {
		
		// get the set
		switch(setInfo.getSetType()) {
		case Fixed:
			return setInfo.getSetValue();
		case TestPercent:
			List<SetInfo> tests = ChallengeDB.getInstance(null).getSets(challengeId, Type.Test);
			if (tests.size() != 0) {
				SetInfo test = tests.get(tests.size() - 1);
				if (test.getValue() != -1) {
					double setPercent = (double)setInfo.getSetValue() / 100;
					double value = test.getValue() * setPercent;
					return (int)(value + 0.5);
				}
			}
		default:
			// we only handle those two cases here
			break;
		}
		
		return -1;
	}
	
	public static int getSetTimeInMinutes(Challenge.SetInfo setInfo) {
		switch (setInfo.getIntervalType()) {
		case Hourly:
			return 60;
		case Daily:
			return 1440;
		case Weekly:
			return 10080;
		case Specific:
			return setInfo.getIntervalValue();
		}
		
		return -1;
	}
}
