package com.doandstevensen.lifecollage;

import android.app.Application;

import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;

/**
 * Created by Sheena on 1/30/17.
 */

public class ThisApplication extends Application {
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/lifecollage";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmLog.setLevel(LogLevel.TRACE);
    }
}
