package com.doandstevensen.lifecollage.util;

import io.realm.SyncUser;

/**
 * Created by Sheena on 1/30/17.
 */

public class RealmUserManager {
    public static void logoutActiveUser() {
        SyncUser.currentUser().logout();
    }

    public static String getCurrentUserId() {
        return SyncUser.currentUser().getIdentity();
    }
}
