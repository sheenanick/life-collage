package com.doandstevenson.lifecollage;

import android.app.Application;

import com.doandstevenson.lifecollage.data.remote.DataManager;

/**
 * Created by Sheena on 1/30/17.
 */

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataManager dataManager = new DataManager(getApplicationContext());
        dataManager.storeScreenSize();
    }
}
