package com.ananda.tailing.bike.smartbike;

import com.ananda.tailing.bike.entity.DeviceInfo;

import android.content.Context;
import android.content.SharedPreferences;


public class DeviceDB {
	// Private constants.
	private final static String BLUE_GUARD_SETTINGS	 = "blue_guard_settings";
	private final static String BLUE_GUARD_SETTINGS_USED = "blue_guard_settings_used";
	private final static String THE_BLUE_GUARD_ID	 = "blue_guard_id";
	private final static String THE_BLUE_GUARD_KEY	 = "blue_guard_key";
	private final static String THE_BLUE_GUARD_NAME	 = "blue_guard_name";

	// Persist last connected BlueGuard.
	public static void saveUsed(Context context, DeviceInfo rec) {
		SharedPreferences sPreferences = context.getSharedPreferences(BLUE_GUARD_SETTINGS, 0);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(THE_BLUE_GUARD_ID, rec.getIdentifier());
		editor.putString(THE_BLUE_GUARD_KEY, rec.getKey());
		editor.putString(THE_BLUE_GUARD_NAME, rec.getName());
		editor.apply();
	}
	
	public static void save(Context context, DeviceInfo rec) {
		SharedPreferences sPreferences = context.getSharedPreferences(BLUE_GUARD_SETTINGS_USED, 0);
		SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(THE_BLUE_GUARD_ID + rec.getIdentifier(), rec.getIdentifier());
		editor.putString(THE_BLUE_GUARD_KEY + rec.getIdentifier(), rec.getKey());
		editor.putString(THE_BLUE_GUARD_NAME + rec.getIdentifier(), rec.getName());
		editor.apply();
	}

	public static DeviceInfo load(Context context,String Identifier) {
		SharedPreferences sPreferences = context.getSharedPreferences(BLUE_GUARD_SETTINGS_USED, 0);
		String identifier = sPreferences.getString(THE_BLUE_GUARD_ID + Identifier, "");
		String name = sPreferences.getString(THE_BLUE_GUARD_NAME + Identifier, "");
		String key = sPreferences.getString(THE_BLUE_GUARD_KEY + Identifier, "");
		
		if(identifier.length() == 0)
			return null;

		DeviceInfo result = new DeviceInfo();
		result.setName(name.length() > 0 ? name : null);
		result.setIdentifier(identifier.length() > 0 ? identifier : null);
		result.setKey(key.length() > 0 ? key : null);
		return result;
	}
	
	public static DeviceInfo loadUsed(Context context) {
		SharedPreferences sPreferences = context.getSharedPreferences(BLUE_GUARD_SETTINGS, 0);
		String identifier = sPreferences.getString(THE_BLUE_GUARD_ID, "");
		String name = sPreferences.getString(THE_BLUE_GUARD_NAME, "");
		String key = sPreferences.getString(THE_BLUE_GUARD_KEY, "");
		
		if(identifier.length() == 0)
			return null;

		DeviceInfo result = new DeviceInfo();
		result.setName(name.length() > 0 ? name : null);
		result.setIdentifier(identifier.length() > 0 ? identifier : null);
		result.setKey(key.length() > 0 ? key : null);
		return result;
	}

}
