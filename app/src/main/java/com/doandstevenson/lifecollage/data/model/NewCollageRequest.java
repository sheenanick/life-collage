package com.doandstevenson.lifecollage.data.model;

/**
 * Created by Sheena on 2/13/17.
 */

public class NewCollageRequest {
    private int id;
    private String title;

    public NewCollageRequest(String title) {
        this.title = title;
        this.id = 0;
    }
}
