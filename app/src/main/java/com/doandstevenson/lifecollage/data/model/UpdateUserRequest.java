package com.doandstevenson.lifecollage.data.model;

/**
 * Created by Sheena on 2/15/17.
 */

public class UpdateUserRequest {
    private int id;
    private String email;

    public UpdateUserRequest(String email, int id) {
        this.email = email;
        this.id = id;
    }
}
