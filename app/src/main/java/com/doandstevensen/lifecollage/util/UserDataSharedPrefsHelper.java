package com.doandstevensen.lifecollage.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.doandstevensen.lifecollage.Constants;
import com.doandstevensen.lifecollage.data.model.ApplicationToken;
import com.doandstevensen.lifecollage.data.model.User;
import com.google.gson.Gson;

import static android.R.attr.width;
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

        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = mContext.getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        editor.putFloat(SCREEN_WIDTH, dpHeight);
        editor.putFloat(SCREEN_HEIGHT, dpWidth);
        editor.commit();
    }

    public Integer getScreenWidth() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.SCREEN_PREFS, Context.MODE_PRIVATE);
        float width = -1;
        if (sharedPrefs.contains(SCREEN_WIDTH)) {
            width = sharedPrefs.getFloat(SCREEN_WIDTH, -1);
        }
        return (int) width;
    }

    public Integer getScreenHeight() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.SCREEN_PREFS, Context.MODE_PRIVATE);
        float height = -1;
        if (sharedPrefs.contains(SCREEN_HEIGHT)) {
            height = sharedPrefs.getFloat(SCREEN_HEIGHT, -1);
        }
        return (int) height;
    }

    public void clearData() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefs.edit().clear().commit();
    }

}
