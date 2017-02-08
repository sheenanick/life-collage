package com.doandstevensen.lifecollage.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sheena on 1/30/17.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String uid;
    private String username;
    private RealmList<Collage> collages = new RealmList<>();

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public RealmList<Collage> getCollages() {
        return collages;
    }

    public void addCollage(Collage collage) {
        collages.add(collage);
    }

}
