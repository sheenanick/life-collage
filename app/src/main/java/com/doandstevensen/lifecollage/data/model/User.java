package com.doandstevensen.lifecollage.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Sheena on 1/30/17.
 */

public class User extends RealmObject {
    private String uid;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private RealmList<Collage> collages = new RealmList<>();

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
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
