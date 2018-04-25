package app.witkey.live.utils.sharedpreferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.witkey.live.WitKeyApplication;

/**
 * created by developer on 10/03/2017.
 */

public class SharedPref {

	public static void clearCache() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	public static String read(String valueKey, String valueDefault) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		return prefs.getString(valueKey, valueDefault);
	}

	public static void save(String valueKey, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(valueKey, value);
		edit.commit();
	}

	public static int read(String valueKey, int valueDefault) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		return prefs.getInt(valueKey, valueDefault);
	}

	public static void save(String valueKey, int value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt(valueKey, value);
		edit.commit();
	}

	public static boolean read(String valueKey, boolean valueDefault) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		return prefs.getBoolean(valueKey, valueDefault);
	}

	public static void save(String valueKey, boolean value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean(valueKey, value);
		edit.commit();
	}

	public static long read(String valueKey, long valueDefault) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		return prefs.getLong(valueKey, valueDefault);
	}

	public static void save(String valueKey, long value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putLong(valueKey, value);
		edit.commit();
	}

	public static float read(String valueKey, float valueDefault) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		return prefs.getFloat(valueKey, valueDefault);
	}

	public static void save(String valueKey, float value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(WitKeyApplication.context);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putFloat(valueKey, value);
		edit.commit();
	}

}
