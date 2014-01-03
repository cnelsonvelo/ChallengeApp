package com.nelsonconsulting.challengeapp.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.nelsonconsulting.challengeapp.Constants.ChallengeStatus;
import com.nelsonconsulting.challengeapp.Constants.Type;
import com.nelsonconsulting.challengeapp.data.ChallengeDBContract.Challenges;
import com.nelsonconsulting.challengeapp.data.ChallengeDBContract.SetEntries;

public class ChallengeDB {

	private static ChallengeDB instance;
	private SQLiteDatabase db;
	
	public class ChallengeInfo {
		String name;
		Calendar startDate;
		ChallengeStatus status;
		int lastInterval;
		
		public String getName() {
			return name;
		}
		
		public Calendar getStartDate() {
			return startDate;
		}
		
		public ChallengeStatus getStatus() {
			return status;
		}
		
		public int getLastInterval() {
			return lastInterval;
		}
		
		private ChallengeInfo(Cursor cursor) {
			this.name = cursor.getString(cursor.getColumnIndex(ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_NAME));
			this.startDate = Calendar.getInstance();
			this.startDate.setTimeInMillis((long)cursor.getDouble(cursor.getColumnIndex(Challenges.COLUMN_NAME_CHALLENGE_START)));
			this.status = ChallengeStatus.fromValue(cursor.getInt(cursor.getColumnIndex(ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_STATE)));
			this.lastInterval = cursor.getInt(cursor.getColumnIndex(ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_LAST));
		}
	}
	
	public class SetInfo {
		private Calendar date;
		private int interval;
		private Type type;
		private int value;
		
		public Calendar getDate() {
			return date;
		}
		
		public int getInterval() {
			return interval;
		}
		
		public Type getType() {
			return type;
		}
		
		public int getValue() {
			return value;
		}
		
		public SetInfo(Cursor cursor) {
			this.date = Calendar.getInstance();
			this.date.setTimeInMillis((long)cursor.getDouble(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_DATETIME)));
			this.interval = cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_INTERVAL));
			this.type = Type.valueOf(cursor.getString(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_TYPE)));
			this.value = cursor.getInt(cursor.getColumnIndex(SetEntries.COLUMN_NAME_ENTRY_VALUE));
		}
	}
	
	public static ChallengeDB getInstance(Context context) {
		if (instance == null) {
			instance = new ChallengeDB(context);
		}
		
		return instance;
	}
	
	private ChallengeDB(Context context) {
		ChallengeDBHelper helper = new ChallengeDBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public void createChallenge(String challengeName) {
		ContentValues v = new ContentValues();
		v.put(Challenges.COLUMN_NAME_CHALLENGE_NAME, challengeName);
		v.put(Challenges.COLUMN_NAME_CHALLENGE_START, Calendar.getInstance().getTimeInMillis());
		v.put(Challenges.COLUMN_NAME_CHALLENGE_STATE, ChallengeStatus.Running.getValue());
		db.insert(Challenges.TABLE_NAME, null, v);
	}
	
	public int getChallengeCount(ChallengeStatus status) {
		
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				new String[] { ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_ID },
				ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_STATE + "=?",
				new String[] { Integer.valueOf(status.getValue()).toString() },
				null,
				null,
				null);
		return result.getCount();
	}
	
	public List<ChallengeInfo> getChallenges(ChallengeStatus status) {
		Cursor result = db.query(ChallengeDBContract.Challenges.TABLE_NAME, 
				null,
				ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_STATE + "=?",
				new String[] { Integer.valueOf(status.getValue()).toString() },
				null,
				null,
				null);
		
		List<ChallengeInfo> challenges = new ArrayList<ChallengeInfo>(); 
		while(result.moveToNext()) {
			ChallengeInfo info = new ChallengeInfo(result);
			challenges.add(info);
		}
		
		return challenges;
	}
	
	public List<SetInfo> getSets(String challengeName, Integer day) {
		
		// query the database
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ChallengeDBContract.Challenges.TABLE_NAME + " c JOIN " +
				SetEntries.TABLE_NAME + " s ON " +
				"s." + ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_ID + " = " + 
				"c." + ChallengeDBContract.SetEntries.COLUMN_NAME_CHALLENGE_ID);
		Cursor result = qb.query(db, 
				null, 
				ChallengeDBContract.Challenges.COLUMN_NAME_CHALLENGE_NAME + " = ? AND " + SetEntries.COLUMN_NAME_INTERVAL + " = ?", 
				new String[] { challengeName, day.toString() }, 
				null, 
				null, 
				null);
		
		// build the results
		List<SetInfo> sets = new ArrayList<SetInfo>();
		while(result.moveToNext()) {
			SetInfo info = new SetInfo(result);
			sets.add(info);
		}
		
		return sets;
	}
}
