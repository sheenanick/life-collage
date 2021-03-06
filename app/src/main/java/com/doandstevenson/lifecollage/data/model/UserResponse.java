package com.doandstevenson.lifecollage.data.model;

/**
 * Created by Sheena on 2/10/17.
 */

public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String created;
    private String updated;

    public int getId() {
        return id;
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

    public String getCreated() {
        return created;
    }

    public String getUpdated() {
        return updated;
    }
}
