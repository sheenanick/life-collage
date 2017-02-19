package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 1/30/17.
 */

public class User {
    private int uid;
    private String username;
    private String email;

    public User() {}

    public User(int uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
