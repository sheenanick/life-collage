package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/17/17.
 */

public class NewPictureRequest {
    private int id;
    private String location;

    public NewPictureRequest(String location) {
        this.id = 0;
        this.location = location;
    }
}
