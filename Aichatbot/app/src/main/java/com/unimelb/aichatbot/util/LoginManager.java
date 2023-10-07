package com.unimelb.aichatbot.util;

import android.content.Context;

public class LoginManager {

    SharedPreferencesHelper sharedPreferencesHelper;


    public LoginManager(Context context) {
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    public void saveLoginInfo(String userId, String username, String accessToken) {

        sharedPreferencesHelper.putString(SharedPreferencesHelper.KEY_USER_ID, userId);
        sharedPreferencesHelper.putString(SharedPreferencesHelper.KEY_USERNAME, username);
        sharedPreferencesHelper.putString(SharedPreferencesHelper.KEY_JWT_TOKEN, accessToken);
    }

    public void logout() {
        sharedPreferencesHelper.remove(SharedPreferencesHelper.KEY_USER_ID);
        sharedPreferencesHelper.remove(SharedPreferencesHelper.KEY_USERNAME);
        sharedPreferencesHelper.remove(SharedPreferencesHelper.KEY_JWT_TOKEN);
    }

    public boolean isLogin() {
        return sharedPreferencesHelper.exists(SharedPreferencesHelper.KEY_JWT_TOKEN) && sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_JWT_TOKEN) != null;
    }

    public String getUserId() {

        return sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USER_ID);
    }

    public String getUsername() {
        return sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME);
    }

    public String getAccessToken() {
        return sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_JWT_TOKEN);
    }

}
