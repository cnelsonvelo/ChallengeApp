package com.nelsonconsulting.challengeapp.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nelsonconsulting.challengeapp.Constants;

import android.content.res.AssetManager;
import android.util.Log;

public class ChallengeCollection {
	
	public static String CHALLENGE_LOCATION = "challenges";

	private AssetManager assets;
	private Map<String, String> challengeFiles;
	
	private static ChallengeCollection instance;
	
	public static ChallengeCollection getInstance(AssetManager mgr) {
		if (instance == null) {
			instance = new ChallengeCollection(mgr);
		}
		
		return instance;
	}
	
	private ChallengeCollection(AssetManager mgr) {
		challengeFiles = new HashMap<String, String>();
		assets = mgr;
		
		loadChallenges();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getChallengeNames() {
		return (List<String>) challengeFiles.keySet();
	}
	
	public Challenge getChallenge(String name) {
		try {
			Challenge challenge = Challenge.parse(assets.open(CHALLENGE_LOCATION + "/" + challengeFiles.get(name)));
			
			return challenge;
		}
		catch (IOException e) {
		}
		
		return null;
	}
	
	private void loadChallenges() {
		
		try {
			// get a list of the files in teh assets folder
			String[] files = assets.list(CHALLENGE_LOCATION);
			
			// loop through the files and get the names
			for (String f : files) {
				Challenge c = Challenge.parse(assets.open(CHALLENGE_LOCATION + "/" + f));
				challengeFiles.put(c.getName(), f);
			}
		}
		catch (IOException ex) {
			Log.e(Constants.APP_LOG_TAG, "Failed to load challenge file", ex);
		}
	}
}
