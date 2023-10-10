package com.unimelb.aichatbot.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    //    login info
    public static final String KEY_USER_ID = "currentUserId";
    public static final String KEY_USERNAME = "currentUsername";
    public static final String KEY_JWT_TOKEN = "jwt_token";


    private final SharedPreferences sp;

    public SharedPreferencesHelper(Context mContext) {
        String PREF_NAME = "app_status";
        this.sp = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public String getString(String key) {
        return sp.getString(key, null);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value).apply();
    }

    public Long getLong(String key) {

        return sp.getLong(key, 0);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value).apply();
    }

    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value).apply();
    }


    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean exists(String key) {
        return sp.contains(key);
    }


}
