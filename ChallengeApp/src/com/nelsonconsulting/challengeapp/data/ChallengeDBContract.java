package com.nelsonconsulting.challengeapp.data;

import android.provider.BaseColumns;

public final class ChallengeDBContract {
	
	public static final String TEXT_TYPE = "TEXT";
	public static final String COMMA_SEP = ",";

	public ChallengeDBContract() {}
	
	public static abstract class SetEntries implements BaseColumns {
		public static final String TABLE_NAME = "SetEntries";
		public static final String COLUMN_NAME_ENTRY_ID = "entryid";
		public static final String COLUMN_NAME_CHALLENGE_ID = "challengeid";
		public static final String COLUMN_NAME_ENTRY_DATETIME = "datetime";
		public static final String COLUMN_NAME_INTERVAL = "interval";
		public static final String COLUMN_NAME_ENTRY_TYPE = "type";
		public static final String COLUMN_NAME_ENTRY_VALUE = "value";
		
		public static final String SQL_CREATE_ENTRIES = 
				"CREATE TABLE " + TABLE_NAME + "(" + 
						COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY," +
						COLUMN_NAME_CHALLENGE_ID + " INTEGER," +
						COLUMN_NAME_ENTRY_DATETIME + " REAL," +
						COLUMN_NAME_INTERVAL + " INTEGER," +
						COLUMN_NAME_ENTRY_TYPE + " INTEGER," +
						COLUMN_NAME_ENTRY_VALUE + " INTEGER" +
						")";
						
		public static final String SQL_DELETE_ENTRIES =
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class Challenges implements BaseColumns {
		public static final String TABLE_NAME = "Challenges";
		public static final String COLUMN_NAME_CHALLENGE_ID = "challengeid";
		public static final String COLUMN_NAME_CHALLENGE_NAME = "challengename";
		public static final String COLUMN_NAME_CHALLENGE_START = "startdate";
		public static final String COLUMN_NAME_CHALLENGE_STATE = "state";
		public static final String COLUMN_NAME_CHALLENGE_LAST = "lastinterval";
		
		public static final String SQL_CREATE_CHALLENGES = 
				"CREATE TABLE " + TABLE_NAME + "(" + 
						COLUMN_NAME_CHALLENGE_ID + " INTEGER PRIMARY KEY," +
						COLUMN_NAME_CHALLENGE_NAME + " TEXT," +
						COLUMN_NAME_CHALLENGE_START + " REAL," +
						COLUMN_NAME_CHALLENGE_STATE + " INTEGER," +
						COLUMN_NAME_CHALLENGE_LAST + " INTEGER" +
						")";
		
		public static final String SQL_DELETE_CHALLENGES =
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}
