package com.doandstevensen.lifecollage.data.model;

import io.realm.RealmObject;

/**
 * Created by Sheena on 1/31/17.
 */

public class Picture extends RealmObject {
    private String path = "https://source.unsplash.com/random";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
