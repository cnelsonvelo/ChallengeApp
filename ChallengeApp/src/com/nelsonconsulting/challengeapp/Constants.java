package com.nelsonconsulting.challengeapp;

public class Constants {

	public static String APP_LOG_TAG = "ChallengeApp";
	
	public final static String PREF_START_TIME = "pref_start_time";
	public final static String PREF_HOURS = "pref_day_hours";
	public final static String DEFAULT_CHALLENGE = "Push-up Challenge";
	
	public enum EndpointType {
		Value,
		Days
	}
	
	/**
	 * The type of set
	 * @author christophern
	 *
	 */
	public enum Type {
		Test(0),
		Normal(1);
		
		private final int value;
		private Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static Type fromValue(int value) {
			switch(value) {
			case 1:
				return Normal;
			default:
				return Test;
			}
		}
	}
	
	public enum SetType {
		Max,
		Min,
		Fixed,
		TestPercent
	}
	
	public enum IntervalType {
		Hourly,
		Daily,
		Weekly,
		Specific
	}
	
	public enum SetStatus {
		New(0),
		Skipped(1),
		Entered(2),
		Notified(3);
		
		private final int value;
		private SetStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static SetStatus fromValue(int value) {
			switch(value) {
			case 1:
				return Skipped;
			case 2:
				return Entered;
			case 3:
				return Notified;
			default:
				return New;
			}
		}
	}
	
	public enum ChallengeStatus {
		Running(0),
		Suspended(1),
		Finished(2);
		
		private final int value;
		private ChallengeStatus(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static ChallengeStatus fromValue(int value) {
			switch(value) {
			case 1:
				return Suspended;
			case 2:
				return Finished;
			default:
				return Running;
			}
		}
	}
}
