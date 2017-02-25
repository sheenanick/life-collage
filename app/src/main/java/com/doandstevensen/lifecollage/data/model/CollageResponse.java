package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/12/17.
 */

public class CollageResponse {
    private int id;
    private String title;
    private int userId;
    private String created;

    public int getCollageId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreated() {
        return created;
    }
}
