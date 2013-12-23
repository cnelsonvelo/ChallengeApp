package com.nelsonconsulting.challengeapp;

public class Constants {

	public static String APP_LOG_TAG = "ChallengeApp";
	
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
		Test,
		Normal
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
}
