package com.nelsonconsulting.challengeapp;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlUtils {

	public static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT){
			result = parser.getText();
			parser.nextTag();
		}
		
		return result;
	}
	
	public static int readInteger(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
		String tmp = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, tag);
		
		return Integer.parseInt(tmp);
	}
	
	public static <T extends Enum<T>> Enum<T> readEnum(XmlPullParser parser, Class<T> enumType, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
		String tmp = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, tag);
		
		return Enum.valueOf(enumType, tmp);
	}
	
	public static String readString(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, tag);
		String tmp = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, tag);
		
		return tmp;
	}
}
