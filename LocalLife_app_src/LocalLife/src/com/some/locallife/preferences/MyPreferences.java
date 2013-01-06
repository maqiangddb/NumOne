package com.some.locallife.preferences;

import com.some.locallife.data.LocalLife;
import com.some.locallife.util.Util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyPreferences {
	public static final String PREFERENCE_CITY_ID = "city_id";
	public static final String PREFERENCE_CITY_NAME = "city_name";
	public static final String PREFERENCE_USER = "user";
	public static final String PREFERENCE_PASSWORD = "password";
	public static final String PREFERENCE_AUTO_LOGIN = "auto_login";

	public static boolean loginUser(LocalLife locallife, String login, String password,
			Editor editor) {
		locallife.setCredentials(login, password);
		editor.putString(PREFERENCE_USER, login);
		editor.putString(PREFERENCE_PASSWORD, password);
		if(!editor.commit()) {
			Util.login("storeLoginPassword commit failed");
			return false;
		}
		return true;
	}

	public static boolean setAutoLogin(Editor editor, boolean auto) {
		editor.putBoolean(PREFERENCE_AUTO_LOGIN, auto);
		if(!editor.commit()) {
			return false;
		}
		return true;
	}

	public static boolean getAutoLogin(SharedPreferences preferences) {
		return preferences.getBoolean(PREFERENCE_AUTO_LOGIN, false);
	}

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
