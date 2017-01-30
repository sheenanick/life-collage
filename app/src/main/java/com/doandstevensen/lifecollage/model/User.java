package com.doandstevensen.lifecollage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sheena on 1/30/17.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String uid;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

}
