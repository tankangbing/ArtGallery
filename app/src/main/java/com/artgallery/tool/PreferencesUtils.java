package com.artgallery.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtils {
	// 存String
	public static void putString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	// 取String
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	}

	// 取String
	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	// 存int
	public static void putInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	// 取int
	public static int getInt(Context context, String key) {
		return getInt(context, key,-1);
	}

	// 取int
	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	// 存boolean
	public static void putBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	// 取boolean
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	// 取boolean
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
}
