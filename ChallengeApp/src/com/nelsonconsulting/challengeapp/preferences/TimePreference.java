package com.nelsonconsulting.challengeapp.preferences;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {

	private static String DEFAULT_VALUE = "08:00";
	
	private static class SavedState extends BaseSavedState {
	    // Member that holds the setting's value
	    // Change this data type to match the type saved by your Preference
	    String value;

	    public SavedState(Parcelable superState) {
	        super(superState);
	    }

	    public SavedState(Parcel source) {
	        super(source);
	        // Get the current preference's value
	        value = source.readString();  // Change this to read the appropriate data type
	    }

	    @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        super.writeToParcel(dest, flags);
	        // Write the preference's value
	        dest.writeString(value);  // Change this to write the appropriate data type
	    }

	    // Standard creator object using an instance of this class
	    public static final Parcelable.Creator<SavedState> CREATOR =
	            new Parcelable.Creator<SavedState>() {

	        public SavedState createFromParcel(Parcel in) {
	            return new SavedState(in);
	        }

	        public SavedState[] newArray(int size) {
	            return new SavedState[size];
	        }
	    };
	}
	
	private int hour;
	private int minute;
	private TimePicker timePicker;
	
	public static int getHour(String time) {
		return Integer.parseInt(time.substring(0, time.indexOf(":")));
	}
	
	public static int getMinute(String time) {
		return Integer.parseInt(time.substring(time.indexOf(":")));
	}
	
	public TimePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
	}
	
	@Override
	public View onCreateDialogView() {
		timePicker = new TimePicker(this.getContext());
		
		return timePicker;
	}
	
	@Override 
	public void onDialogClosed(boolean positiveResult) {
		
		if (positiveResult) {
			persistString(getTime());
		}
		
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		
		String time = null;
		
		if (restorePersistedValue) {
	        // Restore existing state
	        time = getPersistedString(DEFAULT_VALUE);
	        hour = TimePreference.getHour(time);
	        minute = TimePreference.getMinute(time);
	    } else {
	        // Set default state from the XML attribute
	        time = (String) defaultValue;
	        persistString(time);
	    }
		
		hour = TimePreference.getHour(time);
		minute = TimePreference.getMinute(time);
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		String time = a.getString(index);
		if (time == null || time.isEmpty())
			time = DEFAULT_VALUE;
		return time;
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
	    final Parcelable superState = super.onSaveInstanceState();
	    // Check whether this Preference is persistent (continually saved)
	    if (isPersistent()) {
	        // No need to save instance state since it's persistent, use superclass state
	        return superState;
	    }

	    // Create instance of custom BaseSavedState
	    final SavedState myState = new SavedState(superState);
	    // Set the state's value with the class member that holds current setting value
	    myState.value = getTime();
	    return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
	    // Check whether we saved the state in onSaveInstanceState
	    if (state == null || !state.getClass().equals(SavedState.class)) {
	        // Didn't save the state, so call superclass
	        super.onRestoreInstanceState(state);
	        return;
	    }

	    // Cast state to custom BaseSavedState and pass to superclass
	    SavedState myState = (SavedState) state;
	    super.onRestoreInstanceState(myState.getSuperState());
	    
	    // Set this Preference's widget to reflect the restored state
	    timePicker.setCurrentHour(TimePreference.getHour(myState.value));
	    timePicker.setCurrentMinute(TimePreference.getMinute(myState.value));
	}
	
	private String getTime() {
		hour = timePicker.getCurrentHour();
		minute = timePicker.getCurrentMinute();
		
		return String.format(getContext().getResources().getConfiguration().locale, "%d:%d", hour, minute);
	}
}
