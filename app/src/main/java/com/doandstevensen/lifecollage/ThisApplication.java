package com.doandstevensen.lifecollage;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;

/**
 * Created by Sheena on 1/30/17.
 */

public class ThisApplication extends Application {
    public static final String AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
        RealmLog.setLevel(LogLevel.TRACE);
    }
}
