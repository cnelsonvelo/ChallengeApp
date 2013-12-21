package com.nelsonconsulting.challengeapp;

public class Constants {

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