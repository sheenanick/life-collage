package com.doandstevensen.lifecollage.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.User;
import com.google.gson.Gson;

/**
 * Created by Sheena on 2/9/17.
 */

public class UserDataSharedPrefsHelper {
    private Context mContext;

    public UserDataSharedPrefsHelper(Context context) {
        mContext = context;
    }

    public void storeUserData(User user) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String jsonToken = gson.toJson(user);
        editor.putString(Constants.USER_DATA, jsonToken).commit();
    }

    public User getUserData() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        User user = new User();
        if (sharedPrefs.contains(Constants.USER_DATA)) {
            String jsonToken = sharedPrefs.getString(Constants.USER_DATA, null);
            Gson gson = new Gson();
            user = gson.fromJson(jsonToken, User.class);
        }
        return user;
    }

    public void storeUserToken(ApplicationToken token) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String jsonToken = gson.toJson(token);
        editor.putString(Constants.USER_TOKEN, jsonToken).commit();
    }

    public ApplicationToken getUserToken() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        ApplicationToken token = new ApplicationToken();
        if (sharedPrefs.contains(Constants.USER_TOKEN)) {
            String jsonToken = sharedPrefs.getString(Constants.USER_TOKEN, null);
            Gson gson = new Gson();
            token = gson.fromJson(jsonToken, ApplicationToken.class);
        }
        return token;
    }

    public void clearData() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();
    }

}
