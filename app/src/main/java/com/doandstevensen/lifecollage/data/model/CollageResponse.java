package com.doandstevensen.lifecollage.data.model;

/**
 * Created by Sheena on 2/12/17.
 */

public class CollageResponse {
    private int id;
    private String title;
    private int application_user_id;
    private String created;

    public int getCollageId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return application_user_id;
    }

    public String getCreated() {
        return created;
    }
}
