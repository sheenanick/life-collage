package com.doandstevensen.lifecollage.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.User;
import com.google.gson.Gson;

import static com.doandstevensen.lifecollage.Constants.SCREEN_HEIGHT;
import static com.doandstevensen.lifecollage.Constants.SCREEN_PREFS;
import static com.doandstevensen.lifecollage.Constants.SCREEN_WIDTH;

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

    public void storeScreenSize() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(SCREEN_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        editor.putInt(SCREEN_WIDTH, width);
        editor.putInt(SCREEN_HEIGHT, height);
        editor.commit();
    }

    public Integer getScreenWidth() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.SCREEN_PREFS, Context.MODE_PRIVATE);
        Integer width = -1;
        if (sharedPrefs.contains(SCREEN_WIDTH)) {
            width = sharedPrefs.getInt(SCREEN_WIDTH, -1);
        }
        return width;
    }

    public Integer getScreenHeight() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.SCREEN_PREFS, Context.MODE_PRIVATE);
        Integer height = -1;
        if (sharedPrefs.contains(SCREEN_HEIGHT)) {
            height = sharedPrefs.getInt(SCREEN_HEIGHT, -1);
        }
        return height;
    }

    public void clearData() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();
    }

}
