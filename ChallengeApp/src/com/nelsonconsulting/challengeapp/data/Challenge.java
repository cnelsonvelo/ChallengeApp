package com.nelsonconsulting.challengeapp.data;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.SparseArray;
import android.util.Xml;

import com.nelsonconsulting.challengeapp.Constants.EndpointType;
import com.nelsonconsulting.challengeapp.Constants.IntervalType;
import com.nelsonconsulting.challengeapp.Constants.SetType;
import com.nelsonconsulting.challengeapp.Constants.Type;
import com.nelsonconsulting.challengeapp.XmlUtils;

public class Challenge {

	private static String CHALLENGE_TAG = "Challenge";
	private static String NAME_TAG = "Name";
	private static String ENDPOINT_TAG = "EndPoint";
	private static String ENDPOINT_TYPE_TAG = "EndPointType";
	private static String ENDPOINT_VALUE_TAG = "EndPointValue";
	private static String SETS_TAG = "Sets";
	private static String SET_TAG = "Set";
	private static String TYPE_TAG = "Type";
	private static String INDEX_TAG = "IntervalIndex";
	private static String SET_TYPE_TAG = "SetType";
	private static String INTERVAL_TYPE_TAG = "IntervalType";
	private static String INTERVAL_VALUE_TAG = "IntervalValue";
	private static String SET_VALUE_TAG = "SetValue";
	
	public class EndPoint {
		private int value;
		private EndpointType type;
		
		public int getValue() {
			return value;
		}
		
		public void setValue(int value) {
			this.value = value;
		}
		
		public EndpointType getType() {
			return type;
		}
		
		public void setType(EndpointType type) {
			this.type = type;
		}
		
		public void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, ENDPOINT_TAG);
			
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG){
					continue;
				}
				
				// read in the end point bits
				String name = parser.getName();
				if (name.equals(ENDPOINT_TYPE_TAG)) {
					this.type = (EndpointType) XmlUtils.readEnum(parser, EndpointType.class, ENDPOINT_TYPE_TAG);
				}
				else if (name.equals(ENDPOINT_VALUE_TAG)) {
					this.value = XmlUtils.readInteger(parser, ENDPOINT_VALUE_TAG);
				}
			}
			
			parser.require(XmlPullParser.END_TAG, null, ENDPOINT_TAG);
		}		
	}
	
	public class SetInfo {
		private int index;
		private Type type;
		private IntervalType intervalType;
		private SetType setType;
		private int intervalValue;
		private int setValue;
		
		public int getIndex() {
			return index;
		}
		
		public Type getType() {
			return type;
		}
		
		public void setType(Type type) {
			this.type = type;
		}
		
		public IntervalType getIntervalType() {
			return intervalType;
		}
		
		public void setIntervalType(IntervalType intervalType) {
			this.intervalType = intervalType;
		}
		
		public SetType getSetType() {
			return setType;
		}
		
		public void setSetType(SetType setType) {
			this.setType = setType;
		}
		
		public int getIntervalValue() {
			return intervalValue;
		}
		
		public void setIntervalValue(int intervalValue) {
			this.intervalValue = intervalValue;
		}
		
		public int getSetValue() {
			return setValue;
		}
		
		public void setSetValue(int setValue) {
			this.setValue = setValue;
		}
		
		public void parse(XmlPullParser parser) throws XmlPullParserException, IOException {
			parser.require(XmlPullParser.START_TAG, null, SET_TAG);

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG){
					continue;
				}
				
				// read in the end point bits
				String name = parser.getName();
				if (name.equals(INDEX_TAG)) {
					this.index = XmlUtils.readInteger(parser, INDEX_TAG);
				}
				else if (name.equals(TYPE_TAG)) {
					this.type = (Type) XmlUtils.readEnum(parser, Type.class, TYPE_TAG);
				}
				else if (name.equals(INTERVAL_TYPE_TAG)) {
					this.intervalType = (IntervalType) XmlUtils.readEnum(parser, IntervalType.class, INTERVAL_TYPE_TAG);
				}
				else if (name.equals(INTERVAL_VALUE_TAG)) {
					this.intervalValue = XmlUtils.readInteger(parser, INTERVAL_VALUE_TAG);
				}
				else if (name.equals(SET_TYPE_TAG)) {
					this.setType = (SetType) XmlUtils.readEnum(parser, SetType.class, SET_TYPE_TAG);
				}
				else if (name.equals(SET_VALUE_TAG)) {
					this.setValue = XmlUtils.readInteger(parser, SET_VALUE_TAG);
				}
			}
			
			parser.require(XmlPullParser.END_TAG, null, SET_TAG);
		}
	}
	
	private String name;
	private EndPoint endPoint;
	private SparseArray<SetInfo> setInfo;
	
	public String getName() {
		return name;
	}
	
	public EndPoint getEndPoint() {
		return endPoint;
	}
	
	public SparseArray<SetInfo> getSetInfo() {
		return setInfo;
	}
	
	public Challenge() {
		endPoint = new EndPoint();
		setInfo = new SparseArray<SetInfo>();
	}
	
	public static Challenge parse(InputStream stream) {
		Challenge challenge = new Challenge();
		
		try {
			// setup the parser
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.require(XmlPullParser.START_TAG, null, CHALLENGE_TAG);
			
			while(parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				
				String tagName = parser.getName();
				if (tagName.equals(NAME_TAG)) {
					challenge.name = XmlUtils.readString(parser, NAME_TAG);
				}
				else if (tagName.equals(ENDPOINT_TAG)) {
					challenge.endPoint.parse(parser);
				}
				else if (tagName.equals(SETS_TAG)) {
					parseSets(parser, challenge);
				}
			}
		} 
		catch (XmlPullParserException e) {
			return null;
		} 
		catch (IOException e) {
			return null;
		}
		finally {
			
		}
		
		return challenge;
	}
	
	private static void parseSets(XmlPullParser parser, Challenge challenge) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, SETS_TAG);
		
		while(parser.next() != XmlPullParser.END_TAG){
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			if (name.equals(SET_TAG)) {
				SetInfo set = challenge.new SetInfo();
				set.parse(parser);
				challenge.setInfo.put(set.getIndex(), set);
			}
		}
		
		parser.require(XmlPullParser.END_TAG, null, SETS_TAG);
	}
}
