package com.doandstevenson.lifecollage.data.model;

/**
 * Created by Sheena on 2/16/17.
 */

public class UpdateCollageRequest {
    private int id;
    private String title;

    public UpdateCollageRequest(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
