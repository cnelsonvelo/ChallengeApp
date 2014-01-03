package com.nelsonconsulting.challengeapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChallengeDBHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "ChallengeApp";
	private final static int DATABASE_VERSION = 1;
	
	public ChallengeDBHelper(Context context) {
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
}
