package com.doandstevensen.lifecollage.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.google.gson.Gson;

/**
 * Created by Sheena on 2/9/17.
 */

public class UserDataSharedPrefsHelper {

    public UserDataSharedPrefsHelper() {
        super();
    }

    public void storeUserData(Context context, int userId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(Constants.USER_DATA, userId).commit();
    }

    public int getUserData(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int userId = -1;
        if (sharedPrefs.contains(Constants.USER_DATA)) {
            userId = sharedPrefs.getInt(Constants.USER_DATA, -1);
        }
        return userId;
    }

    public void storeUserToken(Context context, ApplicationToken token) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String jsonToken = gson.toJson(token);
        editor.putString(Constants.USER_TOKEN, jsonToken).commit();
    }

    public ApplicationToken getUserToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        ApplicationToken token = new ApplicationToken();
        if (sharedPrefs.contains(Constants.USER_TOKEN)) {
            String jsonToken = sharedPrefs.getString(Constants.USER_TOKEN, null);
            Gson gson = new Gson();
            token = gson.fromJson(jsonToken, ApplicationToken.class);
        }
        return token;
    }

    public void clearData(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();
    }

}
