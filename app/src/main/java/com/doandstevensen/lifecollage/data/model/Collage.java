package com.doandstevensen.lifecollage.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Sheena on 1/31/17.
 */

public class Collage extends RealmObject {
    private String name;
    private RealmList<Picture> pictures = new RealmList<>();
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Picture> getPictures() {
        return pictures;
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    public String getUid() {
        return userId;
    }

    public void setUid(String userId) {
        this.userId = userId;
    }
}
