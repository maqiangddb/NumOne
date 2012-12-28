package com.some.locallife.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreferences {
	public static final String PREFERENCE_CITY_ID = "city_id";
	public static final String PREFERENCE_CITY_NAME = "city_name";

	public static void setData(SharedPreferences preferences, String key, String value) {
		Editor editor = preferences.edit();
		if(!preferences.contains(key)) {
			editor.putString(key, value);
		}
		editor.commit();
	}

	public static void setCityName(SharedPreferences preferences, String cityName) {
		setData(preferences, PREFERENCE_CITY_NAME, cityName);
	}

	public static String getCityName(SharedPreferences preferences) {
		return preferences.getString(PREFERENCE_CITY_NAME, null);
	}

	public static void setCityId(SharedPreferences preferences, String cityId) {
		setData(preferences, PREFERENCE_CITY_ID, cityId);
	}

	public static String getCityId(SharedPreferences preferences) {
		return preferences.getString(PREFERENCE_CITY_ID, null);
	}

}
