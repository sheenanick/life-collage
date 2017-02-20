package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/17/17.
 */

public class PictureResponse {
    private int id;
    private int collageId;
    private String location;
    private String collageTitle;

    public int getId() {
        return id;
    }

    public int getCollageId() {
        return collageId;
    }

    public String getLocation() {
        return location;
    }

    public String getCollageTitle() {
        return collageTitle;
    }

    public void setTitle(String collageTitle) {
        this.collageTitle = collageTitle;
    }
}
