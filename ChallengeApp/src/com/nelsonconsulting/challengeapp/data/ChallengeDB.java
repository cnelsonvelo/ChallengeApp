package com.nelsonconsulting.challengeapp.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nelsonconsulting.challengeapp.Constants;
import com.nelsonconsulting.challengeapp.Constants.ChallengeStatus;
import com.nelsonconsulting.challengeapp.Constants.SetStatus;
import com.nelsonconsulting.challengeapp.Constants.Type;
import com.nelsonconsulting.challengeapp.data.ChallengeDBContract.Challenges;
import com.nelsonconsulting.challengeapp.data.ChallengeDBContract.SetEntries;

public class ChallengeDB extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "ChallengeApp";
	private final static int DATABASE_VERSION = 1;
	
	private static ChallengeDB instance;
	
	/**
	 * Class containing the challenge information contained within the database
	 * @author cnelson
	 *
	 */
	public class ChallengeInfo {
		private int id;
		private String name;
		private Calendar startDate;
		private ChallengeStatus status;
		
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the challenge name
		 * @return
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Gets the start date
		 * @return
		 */
		public Calendar getStartDate() {
			return startDate;
		}
		
		/**
		 * Gets the current status of this challenge
		 * @return
		 */
		public ChallengeStatus getStatus() {
			return status;
		}
		
		/**
		 * Sets the status of the challenge
		 * @param status
		 */
		public void setStatus(ChallengeStatus status) {
			this.status = status;
		}
		
		/**
		 * Constructor taking a cursor and creating a new challenge class
		 * @param cursor
		 */
		private ChallengeInfo(Cursor cursor) {
			this.id = cursor.getInt(cursor.getColumnIndex(Challenges.COLUMN_NAME_CHALLENGE_ID));
			this.name = cursor.getString(cursor.getColumnIndex(Challenges.COLUMN_NAME_CHALLENGE_NAME));
			this.startDate = Calendar.getInstance();
			this.startDate.setTimeInMillis((long)cursor.getDouble(cursor.getColumnIndex(Challenges.COLUMN_NAME_CHALLENGE_START)));
			this.status = ChallengeStatus.fromValue(cursor.getInt(cursor.getColumnIndex(Challenges.COLUMN_NAME_CHALLENGE_STATE)));
		}
	}
	
	/**
	 * Class containg information on the sets contained within the database
	 * @author cnelson
	 *
	 */
	public class SetInfo {
		private int id;
		private Calendar date;
		private int interval;
		private Type type;
		private SetStatus status;
		private int value;
		
		/**
		 * Gets the id of the set
		 * @return
		 */
		public int getId() {
			return id;
		}
		
		/**
		 * Gets the date of the set
		 * @return
		 */
		public Calendar getDate() {
			return date;
		}
		
		/**
		 * Gets the interval of the set
		 * @return
		 */
		public int getInterval() {
			return interval;
		}
		
		/**
		 * Gets the type of the set
		 * @return
		 */
		public Type getType() {
			return type;
		}
		
		/**
		 * Gets the status 
		 * @return
		 */
		public SetStatus getStatus() {
			return status;
		}
		
		/**
		 * Sets the status
		 * @param status
		 */
		public void setStatus(SetStatus status) {
			this.status = status;
		}
		
		/**
		 * Gets the value for the set
		 * @return
		 */
		public int getValue() {
			return value;
		}
		
		/**
		 * Constructor that takes a cursor and creates a new set info class
		 * @param cursor
		 */
		private SetInfo(Cursor cursor) {
			this.id = cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_ID));
			this.date = Calendar.getInstance();
			this.date.setTimeInMillis((long)cursor.getDouble(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_DATETIME)));
			this.interval = cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_INTERVAL));
			this.type = Type.fromValue(cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_TYPE)));
			this.status = SetStatus.fromValue(cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_STATUS)));
			this.value = cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_VALUE));
		}
	}
	
	/**
	 * Gets a singleton instance of the database helper class
	 * @param context context of the application
	 * @return
	 */
	public static ChallengeDB getInstance(Context context) {
		if (instance == null) {
			instance = new ChallengeDB(context);
		}
		
		return instance;
	}
	
	/**
	 * Constructor taking the context 
	 * @param context
	 */
	public ChallengeDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ChallengeDBContract.Challenges.SQL_CREATE_CHALLENGES);
		db.execSQL(ChallengeDBContract.SetEntries.SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// currently there is only a single version
	}
	
	/**
	 * Creates a new challenge in the database, setting the start date to today, and the state to running
	 * @param challengeName
	 * @return ID of the db entry
	 */
	public long createChallenge(String challengeName) {
		Log.v(Constants.APP_LOG_TAG, "Create a new challenge in the db: " + challengeName);
		
		// set the start date to today, but no time
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		
		// check if the challenge name is already in the db in a running state...abandon it
		List<ChallengeInfo> challenges = getChallenges(challengeName, ChallengeStatus.Running);
		for (ChallengeInfo c : challenges) {
			c.setStatus(ChallengeStatus.Suspended);
			updateChallenge(c);
		}
		
		// add the challenge to the database
		ContentValues v = new ContentValues();
		v.put(Challenges.COLUMN_NAME_CHALLENGE_NAME, challengeName);
		v.put(Challenges.COLUMN_NAME_CHALLENGE_START, Calendar.getInstance().getTimeInMillis());
		v.put(Challenges.COLUMN_NAME_CHALLENGE_STATE, ChallengeStatus.Running.getValue());
		
		// get the database and insert the values
		SQLiteDatabase db = this.getWritableDatabase();
		return db.insert(Challenges.TABLE_NAME, null, v);
	}
	
	/**
	 * Adds a set entry to the database
	 * @param challengeId id of the challenge
	 * @param interval interval number (day)
	 * @param type type of set entry (test/normal)
	 * @param value id of the entry
	 * @return
	 */
	public long addSetEntry(long challengeId, int interval, Type type, int value) {
		Log.v(Constants.APP_LOG_TAG, "Adding a set entry to the db");
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues v = new ContentValues();
		v.put(SetEntries.COLUMN_NAME_CHALLENGE_ID, challengeId);
		v.put(SetEntries.COLUMN_NAME_ENTRY_DATETIME, Calendar.getInstance().getTimeInMillis());
		v.put(SetEntries.COLUMN_NAME_ENTRY_TYPE, type.getValue());
		v.put(SetEntries.COLUMN_NAME_ENTRY_VALUE, value);
		v.put(SetEntries.COLUMN_NAME_ENTRY_STATUS, SetStatus.New.getValue());
		v.put(SetEntries.COLUMN_NAME_INTERVAL, interval);
		
		long id = db.insert(SetEntries.TABLE_NAME, null, v);
		
		db.close();
		
		return id;
	}
	
	/**
	 * Gets the number of challenges present in the database of the given status
	 * @param status status of challenges to return
	 * @return number of challenges in the status
	 */
	public int getChallengeCount(ChallengeStatus status) {
		Log.v(Constants.APP_LOG_TAG, "Getting the number of challenges in state: " + status.getValue());
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				new String[] { ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_ID },
				ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_STATE + "=?",
				new String[] { Integer.valueOf(status.getValue()).toString() },
				null,
				null,
				null);
		int count = result.getCount();
		
		db.close();
		
		return count;
	}
	
	/**
	 * Gets a list of challenges in the specified status
	 * @param status status of the challenges to retrieve
	 * @return list of challenges in specified status
	 */
	public List<ChallengeInfo> getChallenges(ChallengeStatus status) {
		Log.v(Constants.APP_LOG_TAG, "Getting all challenges in status: " + status);
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				null,
				Challenges.COLUMN_NAME_CHALLENGE_STATE + "=?",
				new String[] { Integer.valueOf(status.getValue()).toString() },
				null,
				null,
				null);
		
		List<ChallengeInfo> challenges = new ArrayList<ChallengeInfo>(); 
		while(result.moveToNext()) {
			ChallengeInfo info = new ChallengeInfo(result);
			challenges.add(info);
		}
		
		db.close();
		
		return challenges;
	}
	
	/**
	 * Gets a list of challenges in the specified status
	 * @param status status of the challenges to retrieve
	 * @return list of challenges in specified status
	 */
	public List<ChallengeInfo> getChallenges(String challengeName, ChallengeStatus status) {
		Log.v(Constants.APP_LOG_TAG, "Getting all challenges with name/state: " + challengeName + "/" + status);
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				null,
				Challenges.COLUMN_NAME_CHALLENGE_NAME + "=? AND " + Challenges.COLUMN_NAME_CHALLENGE_STATE + "=?",
				new String[] { challengeName, Integer.valueOf(status.getValue()).toString() },
				null,
				null,
				null);
		
		List<ChallengeInfo> challenges = new ArrayList<ChallengeInfo>(); 
		while(result.moveToNext()) {
			ChallengeInfo info = new ChallengeInfo(result);
			challenges.add(info);
		}
		
		db.close();
		
		return challenges;
	}
	
	/**
	 * Gets the specified challenge in the specified status
	 * @param challengeName
	 * @param status
	 * @return
	 */
	public ChallengeInfo getChallenge(int challengeId) {
		Log.v(Constants.APP_LOG_TAG, "Getting a specified challenge from the db: " + challengeId);
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				null,
				Challenges.COLUMN_NAME_CHALLENGE_ID + "=?",
				new String[] { Integer.valueOf(challengeId).toString() },
				null,
				null,
				null);
		
		ChallengeInfo info = null;
		while(result.moveToNext()) {
			info = new ChallengeInfo(result);
		}
		
		db.close();
		
		return info;
	}
	
	public List<SetInfo> getSets(int challengeId, Integer day) {
		Log.v(Constants.APP_LOG_TAG, "Getting all sets for challenge/day: " + challengeId + "/" + day );
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		// query the database
		Cursor result = db.query(SetEntries.TABLE_NAME, 
				null, 
				SetEntries.COLUMN_NAME_CHALLENGE_ID + " = ? AND " + 
						SetEntries.COLUMN_NAME_INTERVAL + " = ?",
				new String[] { String.valueOf(challengeId), 
							   String.valueOf(day) }, 
				null, 
				null, 
				null);
		
		// build the results
		List<SetInfo> sets = new ArrayList<SetInfo>();
		while(result.moveToNext()) {
			SetInfo info = new SetInfo(result);
			sets.add(info);
		}
		
		db.close();
		
		return sets;
	}
	
	public List<SetInfo> getSets(int challengeId, Type type) {
		Log.v(Constants.APP_LOG_TAG, "Getting all sets for challenge/type: " + challengeId + "/" + type );
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		// query the database
		Cursor result = db.query(SetEntries.TABLE_NAME, 
				null, 
				SetEntries.COLUMN_NAME_CHALLENGE_ID + " = ? AND " + 
						SetEntries.COLUMN_NAME_ENTRY_TYPE + " = ?",
				new String[] { String.valueOf(challengeId), 
							   String.valueOf(type.getValue()) }, 
				null, 
				null, 
				null);
		
		// build the results
		List<SetInfo> sets = new ArrayList<SetInfo>();
		while(result.moveToNext()) {
			SetInfo info = new SetInfo(result);
			sets.add(info);
		}
		
		db.close();
		
		return sets;
	}
	
	/**
	 * Gets a list of sets for the specified challenge for the specified day
	 * @param challengeId 
	 * @param day day
	 * @return list of sets
	 */
	public List<SetInfo> getSets(int challengeId, Integer day, Type type) {
		Log.v(Constants.APP_LOG_TAG, "Getting all sets for challenge/day/type: " + challengeId + "/" + day + "/" + type);
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		// query the database
		Cursor result = db.query(SetEntries.TABLE_NAME, 
				null, 
				SetEntries.COLUMN_NAME_CHALLENGE_ID + " = ? AND " + 
						SetEntries.COLUMN_NAME_INTERVAL + " = ? AND " + 
						SetEntries.COLUMN_NAME_ENTRY_TYPE + "=?",
				new String[] { String.valueOf(challengeId), 
							   String.valueOf(day), 
							   String.valueOf(type.getValue()) }, 
				null, 
				null, 
				null);
		
		// build the results
		List<SetInfo> sets = new ArrayList<SetInfo>();
		while(result.moveToNext()) {
			SetInfo info = new SetInfo(result);
			sets.add(info);
		}
		
		db.close();
		
		return sets;
	}
	
	/**
	 * Gets a list of sets for the specified challenge for the specified day
	 * @param challengeId 
	 * @param day day
	 * @return list of sets
	 */
	public List<SetInfo> getSets(int challengeId, Integer day, Type type, SetStatus status) {
		Log.v(Constants.APP_LOG_TAG, "Getting all sets for challenge/day/type: " + challengeId + "/" + day + "/" + type);
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		// query the database
		Cursor result = db.query(SetEntries.TABLE_NAME, 
				null, 
				SetEntries.COLUMN_NAME_CHALLENGE_ID + " = ? AND " + 
						SetEntries.COLUMN_NAME_INTERVAL + " = ? AND " + 
						SetEntries.COLUMN_NAME_ENTRY_TYPE + "=? AND " + 
						SetEntries.COLUMN_NAME_ENTRY_STATUS + "=?", 
				new String[] { String.valueOf(challengeId), 
							   String.valueOf(day), 
							   String.valueOf(type.getValue()), 
							   String.valueOf(status.getValue()) }, 
				null, 
				null, 
				null);
		
		// build the results
		List<SetInfo> sets = new ArrayList<SetInfo>();
		while(result.moveToNext()) {
			SetInfo info = new SetInfo(result);
			sets.add(info);
		}
		
		db.close();
		
		return sets;
	}
	
	/**
	 * Updates the challenge status
	 * @param challengeId
	 * @param status
	 */
	public void updateChallenge(ChallengeInfo info) {
		Log.v(Constants.APP_LOG_TAG, "Updating challenge/status in db: " + info.getId() + "/" + info.getStatus());
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues v = new ContentValues();
		v.put(Challenges.COLUMN_NAME_CHALLENGE_STATE, info.getStatus().getValue());
		
		db.update(Challenges.TABLE_NAME, v, Challenges.COLUMN_NAME_CHALLENGE_ID + "=?", new String[] { String.valueOf(info.getId()) });
		
		db.close();
	}
	
	/**
	 * Updates the set with a new state
	 * @param info
	 */
	public void updateSet(SetInfo info) {
		Log.v(Constants.APP_LOG_TAG, "Updating set in db: " + info.getId() + "/" + info.getStatus());
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues v = new ContentValues();
		v.put(SetEntries.COLUMN_NAME_ENTRY_STATUS, info.getStatus().getValue());
		v.put(SetEntries.COLUMN_NAME_ENTRY_VALUE, info.getValue());
		
		db.update(SetEntries.TABLE_NAME, v, SetEntries.COLUMN_NAME_ENTRY_ID + "=?", new String[] { String.valueOf(info.getId()) });
		
		db.close();
	}
}
